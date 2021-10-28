package com.armand.ourhome.market.voucher.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.VoucherType;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class VoucherServiceTest {

  @Autowired
  private VoucherService voucherService;

  @Autowired
  private VoucherRepository<Voucher> voucherRepository;

  @AfterEach
  void tearDown() {
    voucherRepository.deleteAll();
  }

  @Test
  @DisplayName("바우처를 저장할 수 있다")
  void testSaveVoucher() {
    // given
    RequestVoucher request = RequestVoucher.builder()
        .value(2000)
        .minLimit(10000)
        .voucherType(VoucherType.FIXED)
        .build();

    // when
    VoucherDto save = voucherService.save(request);

    // then
    assertThat(voucherRepository.count()).isEqualTo(1);
  }

}