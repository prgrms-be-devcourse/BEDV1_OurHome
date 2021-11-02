package com.armand.ourhome.market.voucher.repository;

import com.armand.ourhome.market.voucher.domain.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

  Optional<Wallet> findByUserIdAndVoucherId(Long userId, Long voucherId);

  boolean existsByUserIdAndVoucherId(Long userId, Long voucherId);

}
