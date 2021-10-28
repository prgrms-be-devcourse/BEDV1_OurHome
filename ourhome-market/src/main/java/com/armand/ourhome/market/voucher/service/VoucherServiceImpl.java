package com.armand.ourhome.market.voucher.service;

import com.armand.ourhome.market.voucher.dto.RequestCreateFixedVoucher;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoucherServiceImpl implements VoucherService {

  private final VoucherRepository voucherRepository;

}
