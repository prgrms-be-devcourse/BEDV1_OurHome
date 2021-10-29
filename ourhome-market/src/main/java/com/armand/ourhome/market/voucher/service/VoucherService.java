package com.armand.ourhome.market.voucher.service;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.ErrorCode;
import com.armand.ourhome.market.voucher.converter.VoucherConverter;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.exception.DuplicateVoucherException;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoucherService {

  private final VoucherRepository voucherRepository;
  private final VoucherConverter voucherConverter;

  @Transactional
  public VoucherDto save(RequestVoucher request) {
    Optional<Voucher> saved = switch (request.getVoucherType()) {
      case FIXED -> voucherRepository.findByAmountAndMinLimit(request.getValue(),
          request.getMinLimit());
      case PERCENT -> voucherRepository.findByPercentAndMinLimit(request.getValue(),
          request.getMinLimit());
    };

    if (saved.isPresent()) {
      throw new DuplicateVoucherException("이미 중복된 바우처가 있습니다", ErrorCode.INVALID_INPUT_VALUE);
    }

    Voucher voucher = voucherConverter.toEntity(request);
    voucherRepository.save(voucher);
    return voucherConverter.toDto(voucher);
  }

}
