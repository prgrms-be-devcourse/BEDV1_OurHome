package com.armand.ourhome.market.voucher.service;

import com.armand.ourhome.market.voucher.converter.VoucherConverter;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.exception.DuplicateVoucherException;
import com.armand.ourhome.market.voucher.exception.VoucherNotFoundException;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import java.text.MessageFormat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoucherService {

  private final VoucherRepository<Voucher> voucherRepository;
  private final VoucherConverter voucherConverter;

  public Page<VoucherDto> lookUp(Pageable pageable) {
    Page<VoucherDto> allVouchers = voucherRepository.findAll(pageable)
        .map(voucher -> voucherConverter.toDto(voucher));
    return allVouchers;
  }

  @Transactional
  public VoucherDto save(RequestVoucher request) {
    validateDuplicateVoucher(request);

    Voucher voucher = voucherConverter.toEntity(request);
    voucherRepository.save(voucher);
    return voucherConverter.toDto(voucher);
  }

  @Transactional
  public VoucherDto update(Long id, RequestVoucher request) {
    // 같은 id값의 바우처가 존재하는지 확인
    validateExistId(id);
    // 수정하고자하는 정보와 같은 바우처가 존재하는지 확인
    validateDuplicateVoucher(request);
    // 수정하고자하는 바우처의 타입과 기존 바우처의 타입이 동일한지 확인
    Voucher voucher = voucherRepository.findById(id).get();
    request.getVoucherType().validateVoucherType(voucher);

    voucher.update(request.getValue(), request.getMinLimit());
    voucherRepository.flush(); // updatedAt 반영
    return voucherConverter.toDto(voucher);
  }

  @Transactional
  public void delete(Long id) {
    validateExistId(id);

    Voucher voucher = voucherRepository.findById(id).get();
    voucherRepository.delete(voucher);
  }

  private void validateExistId(Long id) {
    boolean existsById = voucherRepository.existsById(id);

    if (!existsById) {
      throw new VoucherNotFoundException(MessageFormat.format("등록된 바우처를 찾을 수 없습니다. id : {0}", id));
    }
  }

  private void validateDuplicateVoucher(RequestVoucher request) {
    Optional<Voucher> saved = findDuplicatedVoucher(request);

    if (saved.isPresent()) {
      throw new DuplicateVoucherException("이미 중복된 바우처가 있습니다");
    }
  }

  private Optional<Voucher> findDuplicatedVoucher(RequestVoucher request) {
    return switch (request.getVoucherType()) {
      case FIXED -> voucherRepository.findByAmountAndMinLimit(request.getValue(),
          request.getMinLimit());
      case PERCENT -> voucherRepository.findByPercentAndMinLimit(request.getValue(),
          request.getMinLimit());
    };
  }

}
