package com.armand.ourhome.market.voucher.service;

import com.armand.ourhome.market.voucher.converter.VoucherConverter;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.exception.DifferentTypeVoucherException;
import com.armand.ourhome.market.voucher.exception.DuplicateVoucherException;
import com.armand.ourhome.market.voucher.exception.VoucherNotFoundException;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoucherService {

  private static final String MESSAGE_DUPLICATE_VOUCHER = "이미 중복된 바우처가 있습니다";
  private static final String MESSAGE_VOUCHER_NOT_FOUND = "등록된 바우처를 찾을 수 없습니다";
  private static final String MESSAGE_VOUCHER_TYPE_NOT_SAME = "바우처의 타입이 동일하지 않습니다";

  private final VoucherRepository<Voucher> voucherRepository;
  private final VoucherConverter voucherConverter;
  
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
    Voucher voucher = validateExistId(id);
    // 수정하고자하는 정보와 같은 바우처가 존재하는지 확인
    validateDuplicateVoucher(request);
    // 수정하고자하는 바우처의 타입과 기존 바우처의 타입이 동일한지 확인
    validateVoucherType(request, voucher);

    voucher.update(request.getValue(), request.getMinLimit());
    voucherRepository.flush(); // updatedAt 반영
    return voucherConverter.toDto(voucher);
  }

  @Transactional
  public void delete(Long id) {
    Voucher voucher = validateExistId(id);
    voucherRepository.delete(voucher);
  }

  private Voucher validateExistId(Long id) {
    Optional<Voucher> byId = voucherRepository.findById(id);

    if (byId.isEmpty()) {
      throw new VoucherNotFoundException(MESSAGE_VOUCHER_NOT_FOUND);
    }

    return byId.get();
  }

  private void validateDuplicateVoucher(RequestVoucher request) {
    Optional<Voucher> saved = findDuplicatedVoucher(request);

    if (saved.isPresent()) {
      throw new DuplicateVoucherException(MESSAGE_DUPLICATE_VOUCHER);
    }
  }

  private void validateVoucherType(RequestVoucher request, Voucher voucher) {
    boolean differentType = request.getVoucherType().isDifferentType(voucher);

    if (differentType) {
      throw new DifferentTypeVoucherException(MESSAGE_VOUCHER_TYPE_NOT_SAME);
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
