package com.armand.ourhome.market.voucher.converter;

import com.armand.ourhome.market.voucher.domain.Wallet;
import com.armand.ourhome.market.voucher.dto.WalletDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WalletMapper {

  VoucherConverter voucherConverter = new VoucherConverter();

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "voucherDto", expression = "java(voucherConverter.toDto(wallet.getVoucher()))")
  WalletDto toDto(Wallet wallet);

}
