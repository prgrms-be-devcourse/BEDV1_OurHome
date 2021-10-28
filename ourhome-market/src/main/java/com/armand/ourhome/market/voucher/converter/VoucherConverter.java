package com.armand.ourhome.market.voucher.converter;

import com.armand.ourhome.market.voucher.domain.FixedVoucher;
import com.armand.ourhome.market.voucher.domain.PercentVoucher;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.VoucherType;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import java.text.MessageFormat;
import org.springframework.stereotype.Component;

@Component
public class VoucherConverter {

  public Voucher toEntity(RequestVoucher request) {
    if (VoucherType.FIXED.equals(request.getVoucherType())) {
      return FixedVoucher.of(request.getValue(), request.getMinLimit());
    }
    if (VoucherType.PERCENT.equals(request.getVoucherType())) {
      return PercentVoucher.of(request.getValue(), request.getMinLimit());
    }
    throw new IllegalArgumentException(
        MessageFormat.format("잘못된 바우처 타입입니다. request.voucherType : {0}", request.getVoucherType()));
  }

  public VoucherDto toDto(Voucher voucher) {
    if (FixedVoucher.class.equals(voucher.getClass())) {
      return VoucherDto.builder()
          .value(((FixedVoucher) voucher).getAmount())
          .minLimit(voucher.getMinLimit())
          .voucherType(VoucherType.FIXED)
          .createdAt(voucher.getCreatedAt())
          .build();
    }
    if (PercentVoucher.class.equals(voucher.getClass())) {
      return VoucherDto.builder()
          .value(((PercentVoucher) voucher).getPercent())
          .minLimit(voucher.getMinLimit())
          .voucherType(VoucherType.PERCENT)
          .createdAt(voucher.getCreatedAt())
          .build();
    }
    throw new IllegalArgumentException(
        MessageFormat.format("잘못된 바우처 타입입니다. voucher.voucherType : {0}", voucher.getClass()));
  }

}
