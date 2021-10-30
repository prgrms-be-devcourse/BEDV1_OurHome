package com.armand.ourhome.market.voucher.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.VoucherType;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.exception.DifferentTypeVoucherException;
import com.armand.ourhome.market.voucher.exception.DuplicateVoucherException;
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

  private RequestVoucher request = RequestVoucher.builder()
      .value(2000)
      .minLimit(10000)
      .voucherType(VoucherType.FIXED)
      .build();

  @AfterEach
  void tearDown() {
    voucherRepository.deleteAll();
  }

  @Test
  @DisplayName("바우처를 저장할 수 있다")
  void testSaveVoucher() {
    // given

    // when
    VoucherDto save = voucherService.save(request);

    // then
    assertThat(voucherRepository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("중복된 바우처는 저장할 수 없다")
  void cannot_save_duplicatedVoucher() {
    // given
    voucherService.save(request);

    // when
    RequestVoucher duplicatedRequest = RequestVoucher.builder()
        .value(2000)
        .minLimit(10000)
        .voucherType(VoucherType.FIXED)
        .build();

    // then
    assertThrows(BusinessException.class, () -> {
      voucherService.save(duplicatedRequest);
    });
  }

  @Test
  @DisplayName("바우처를 정상적으로 수정할 수 있다")
  void testUpdate() {
    // given
    VoucherDto save = voucherService.save(request);

    RequestVoucher updatedVoucher = RequestVoucher.builder()
        .value(3000)
        .minLimit(20000)
        .voucherType(VoucherType.FIXED)
        .build();

    // when
    VoucherDto update = voucherService.update(save.getId(), updatedVoucher);

    // then
    assertThat(update.getValue()).isEqualTo(updatedVoucher.getValue());
    assertThat(update.getMinLimit()).isEqualTo(updatedVoucher.getMinLimit());
  }

  @Test
  @DisplayName("이미 동일한 정보의 바우처가 존재한다면 기존 바우처를 수정할 수 없다")
  void cannot_updateVoucher_ifAlreadyExist() {
    // given
    VoucherDto save = voucherService.save(request);

    RequestVoucher duplicatedRequest = RequestVoucher.builder()
        .value(2000)
        .minLimit(10000)
        .voucherType(VoucherType.FIXED)
        .build();

    // then
    assertThrows(DuplicateVoucherException.class, () -> {
      // when
      voucherService.update(save.getId(), duplicatedRequest);
    });
  }

  @Test
  @DisplayName("바우처 타입을 수정할 수 없다")
  void cannot_updateVoucherType() {
    // given
    VoucherDto save = voucherService.save(request);

    RequestVoucher updatedRequest = RequestVoucher.builder()
        .value(20)
        .minLimit(10000)
        .voucherType(VoucherType.PERCENT)
        .build();

    // then
    assertThrows(DifferentTypeVoucherException.class, () -> {
      // when
      voucherService.update(save.getId(), updatedRequest);
    });
  }

}