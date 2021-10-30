package com.armand.ourhome.market.voucher.controller;

import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.service.VoucherService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<VoucherDto> save(@Valid @RequestBody RequestVoucher request) {
    return ResponseEntity.ok(voucherService.save(request));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<VoucherDto> update(@PathVariable Long id,
      @Valid @RequestBody RequestVoucher request) {
    return ResponseEntity.ok(voucherService.update(id, request));
  }

}
