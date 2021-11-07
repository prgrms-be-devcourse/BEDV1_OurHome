package com.armand.ourhome.market.voucher.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.VoucherType;
import com.armand.ourhome.market.voucher.dto.WalletDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.exception.DifferentTypeVoucherException;
import com.armand.ourhome.market.voucher.exception.DuplicateVoucherException;
import com.armand.ourhome.market.voucher.exception.VoucherNotFoundException;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
class VoucherServiceTest {

  @Autowired
  private VoucherService voucherService;

  @Autowired
  private VoucherRepository<Voucher> voucherRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  void testSaveVoucher() {
    // given
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(1000)
        .minLimit(10000)
        .voucherType(VoucherType.FIXED)
        .build();

    // when
    VoucherDto save = voucherService.save(requestVoucher);

    // then
    assertThat(voucherRepository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("저장된 바우처를 불러올 수 있다")
  void testLookUp() {
    // given
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(2000)
        .minLimit(10000)
        .voucherType(VoucherType.FIXED)
        .build();

    voucherService.save(requestVoucher);

    // when
    PageRequest page = PageRequest.of(0, 10);
    PageResponse<List<VoucherDto>> voucherPage = voucherService.lookUp(page);

    // then
    assertThat(voucherPage.getTotalElements()).isEqualTo(voucherRepository.count());
  }

  @Test
  @DisplayName("중복된 바우처는 저장할 수 없다")
  void cannot_save_duplicatedVoucher() {
    // given
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(3000)
        .minLimit(30000)
        .voucherType(VoucherType.FIXED)
        .build();

    voucherService.save(requestVoucher);

    // when
    RequestVoucher duplicatedRequest = RequestVoucher.builder()
        .value(3000)
        .minLimit(30000)
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
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(4000)
        .minLimit(30000)
        .voucherType(VoucherType.FIXED)
        .build();

    VoucherDto save = voucherService.save(requestVoucher);

    RequestVoucher updatedVoucher = RequestVoucher.builder()
        .value(4000)
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
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(5000)
        .minLimit(10000)
        .voucherType(VoucherType.FIXED)
        .build();

    VoucherDto save = voucherService.save(requestVoucher);

    RequestVoucher duplicatedRequest = RequestVoucher.builder()
        .value(5000)
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
  @DisplayName("바우처를 정상적으로 삭제할 수 있다")
  void testDelete() {
    // given
    long countBeforeDelete = voucherRepository.count();

    // when
    voucherService.delete(1L);

    // then
    assertThat(voucherRepository.count()).isEqualTo(countBeforeDelete - 1);
  }

  @Test
  @DisplayName("존재하지 않는 id값의 바우처는 삭제할 수 없다")
  void cannot_deleteVoucher_withIncorrectId() {
    // given
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(33)
        .minLimit(30000)
        .voucherType(VoucherType.PERCENT)
        .build();

    VoucherDto save = voucherService.save(requestVoucher);

    // then
    assertThrows(VoucherNotFoundException.class, () -> {
      // when
      voucherService.delete(100L);
    });
  }

  @Test
  @DisplayName("바우처를 유저에게 할당할 수 있다")
  void testAssignToUser() {
    // given
    User user = User.builder()
        .address("경기도 성남시")
        .description("useruser")
        .email("user@gmail.com")
        .nickname("nickname")
        .password("pw123!")
        .profileImageUrl("imageUrl")
        .createdAt(LocalDateTime.now())
        .build();

    userRepository.save(user);

    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(7000)
        .minLimit(100000)
        .voucherType(VoucherType.FIXED)
        .build();

    VoucherDto voucherDto = voucherService.save(requestVoucher);

    // when
    WalletDto walletDto = voucherService.assignToUser(voucherDto.getId(), user.getId());

    // then
    assertThat(walletDto).isNotNull();
    assertThat(walletDto.getUserId()).isEqualTo(user.getId());
    assertThat(walletDto.getVoucherDto().getId()).isEqualTo(voucherDto.getId());
  }

}