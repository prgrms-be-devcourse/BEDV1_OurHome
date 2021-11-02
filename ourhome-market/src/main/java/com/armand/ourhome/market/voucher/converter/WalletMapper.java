package com.armand.ourhome.market.voucher.converter;

import com.armand.ourhome.market.voucher.domain.Wallet;
import com.armand.ourhome.market.voucher.dto.WalletDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WalletMapper {

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "voucherDto", source = "voucher")
  WalletDto toDto(Wallet wallet);

}
