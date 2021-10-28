package com.armand.ourhome.market.voucher.repository;

import com.armand.ourhome.market.voucher.domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository<T extends Voucher> extends JpaRepository<T, Long> {

}
