package com.armand.ourhome.market.voucher.repository;

import com.armand.ourhome.market.voucher.domain.Voucher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoucherRepository<T extends Voucher> extends JpaRepository<T, Long> {

  @Query("select v from PercentVoucher v where v.percent = :percent and v.minLimit = :minLimit")
  Optional<Voucher> findByPercentAndMinLimit(@Param("percent") Integer percent,
      @Param("minLimit") Integer minLimit);

  @Query("select v from FixedVoucher v where v.amount = :amount and v.minLimit = :minLimit")
  Optional<Voucher> findByAmountAndMinLimit(@Param("amount") Integer amount,
      @Param("minLimit") Integer minLimit);

}
