//package com.armand.ourhome.market.voucher.mapper;
//
//import com.armand.ourhome.market.voucher.domain.FixedVoucher;
//
//import com.armand.ourhome.market.voucher.domain.PercentVoucher;
//import com.armand.ourhome.market.voucher.domain.Voucher;
//import com.armand.ourhome.market.voucher.dto.VoucherDto;
//import com.armand.ourhome.market.voucher.dto.VoucherType;
//import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
//import org.mapstruct.Mapper;
//
//@Mapper
//public interface VoucherMapper {
//
//  default Voucher requestToVoucher(RequestVoucher requestVoucher){
//
//    }
//  }
//
//  default <T extends Voucher> VoucherDto toVoucherDto(T voucher) {
//    VoucherDto voucherDto = new VoucherDto();
//
//    if (voucher.getClass().equals(FixedVoucher.class)) {
//      voucherDto = VoucherDto.builder()
//          .percent(null)
//          .amount(((FixedVoucher) voucher).getAmount())
//          .minLimit(voucher.getMinLimit())
//          .voucherType(VoucherType.FIXED)
//          .createdAt(voucher.getCreatedAt())
//          .build();
//    }
//    if (voucher.getClass().equals(PercentVoucher.class)) {
//      voucherDto = VoucherDto.builder()
//          .percent(((PercentVoucher) voucher).getPercent())
//          .amount(null)
//          .minLimit(voucher.getMinLimit())
//          .voucherType(VoucherType.PERCENT)
//          .createdAt(voucher.getCreatedAt())
//          .build();
//    }
//
//    return voucherDto;
//  }
//
//}
