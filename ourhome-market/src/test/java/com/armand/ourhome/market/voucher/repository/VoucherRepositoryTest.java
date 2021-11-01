package com.armand.ourhome.market.voucher.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.market.voucher.domain.FixedVoucher;
import com.armand.ourhome.market.voucher.domain.Voucher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(OurHomeDomainConfig.class)
@DataJpaTest
class VoucherRepositoryTest {

  @Autowired
  private VoucherRepository<Voucher> voucherRepository;

  @Test
  @DisplayName("바우처를 저장할 수 있다")
  void testSaveVoucher() {
    // given
    FixedVoucher fixedVoucher = FixedVoucher.of(1000, 2000);

    // when
    FixedVoucher save = voucherRepository.save(fixedVoucher);

    // then
    assertThat(voucherRepository.count()).isEqualTo(1);
  }

}