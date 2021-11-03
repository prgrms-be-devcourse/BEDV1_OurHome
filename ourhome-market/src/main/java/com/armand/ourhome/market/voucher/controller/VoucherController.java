package com.armand.ourhome.market.voucher.controller;

import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.WalletDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.service.VoucherService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/vouchers")
@RestController
public class VoucherController {

  private final VoucherService voucherService;

  @GetMapping
  public ResponseEntity<Page<VoucherDto>> lookUp(Pageable pageable) {
    return ResponseEntity.ok(voucherService.lookUp(pageable));
  }

  @PostMapping
  public ResponseEntity<VoucherDto> save(@Valid @RequestBody RequestVoucher request) {
    return ResponseEntity.ok(voucherService.save(request));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<VoucherDto> update(@PathVariable Long id,
      @Valid @RequestBody RequestVoucher request) {
    return ResponseEntity.ok(voucherService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    voucherService.delete(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/assign-to-user")
  public ResponseEntity<WalletDto> assignToUser(@PathVariable Long id, @RequestParam Long userId) {
    return ResponseEntity.ok(voucherService.assignToUser(id, userId));
  }
//
//  @DeleteMapping("/{id}/use-voucher")
//  public ResponseEntity<Void> use(@PathVariable Long id, @RequestParam Long userId) {
//    voucherService.use(id, userId);
//    return ResponseEntity.ok().build();
//  }

}
