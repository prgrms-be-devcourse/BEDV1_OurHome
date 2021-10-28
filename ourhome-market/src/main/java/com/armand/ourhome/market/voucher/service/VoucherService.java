package com.armand.ourhome.market.voucher.service;

import com.armand.ourhome.market.voucher.converter.VoucherConverter;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
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
    Voucher voucher = voucherConverter.toEntity(request);

    voucherRepository.save(voucher);

    return voucherConverter.toDto(voucher);
  }

}
