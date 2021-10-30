package com.armand.ourhome.market.voucher.controller;

import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/vouchers")
@RestController
public class VoucherController {

  private final VoucherService voucherService;

  @PostMapping
  public ResponseEntity<VoucherDto> save(@RequestBody RequestVoucher request) {
    return ResponseEntity.ok(voucherService.save(request));
  }

}
