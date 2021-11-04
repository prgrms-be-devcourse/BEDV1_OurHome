package com.armand.ourhome.market.voucher.service;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.voucher.converter.VoucherConverter;
import com.armand.ourhome.market.voucher.converter.WalletMapper;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.domain.Wallet;
import com.armand.ourhome.market.voucher.dto.PageResponse;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.WalletDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.exception.DuplicateVoucherException;
import com.armand.ourhome.market.voucher.exception.DuplicateWalletException;
import com.armand.ourhome.market.voucher.exception.VoucherNotFoundException;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import com.armand.ourhome.market.voucher.repository.WalletRepository;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoucherService {

  private final VoucherRepository<Voucher> voucherRepository;
  private final VoucherConverter voucherConverter;

  private final WalletRepository walletRepository;
  private final UserRepository userRepository;
  private final WalletMapper walletMapper = Mappers.getMapper(WalletMapper.class);

  public PageResponse<List<VoucherDto>> lookUp(Pageable pageable) {
    Page<VoucherDto> pages = voucherRepository.findAll(pageable)
        .map(voucher -> voucherConverter.toDto(voucher));

    List<VoucherDto> allVouchers = voucherRepository.findAll().stream()
        .map(voucher -> voucherConverter.toDto(voucher)).collect(Collectors.toList());

    return new PageResponse<>(allVouchers, pages.getTotalElements(), pages.getTotalPages(),
        pages.getSize());
  }

  @Transactional
  public VoucherDto save(RequestVoucher request) {
    validateDuplicateVoucher(request);

    Voucher voucher = voucherConverter.toEntity(request);
    voucherRepository.save(voucher);
    return voucherConverter.toDto(voucher);
  }

  @Transactional
  public VoucherDto update(Long id, RequestVoucher request) {
    // 같은 id값의 바우처가 존재하는지 확인
    validateExistVoucherById(id);
    // 수정하고자하는 정보와 같은 바우처가 존재하는지 확인
    validateDuplicateVoucher(request);
    // 수정하고자하는 바우처의 타입과 기존 바우처의 타입이 동일한지 확인
    Voucher voucher = voucherRepository.findById(id).get();
    request.getVoucherType().validateVoucherType(voucher);

    voucher.update(request.getValue(), request.getMinLimit());
    voucherRepository.flush(); // updatedAt 반영
    return voucherConverter.toDto(voucher);
  }

  @Transactional
  public void delete(Long id) {
    validateExistVoucherById(id);

    Voucher voucher = voucherRepository.findById(id).get();
    voucherRepository.delete(voucher);
  }

  @Transactional
  public WalletDto assignToUser(Long id, Long userId) {
    validateExistVoucherById(id);
    validateExistUserById(userId);
    validateDuplicatedWallet(id, userId);

    Voucher voucher = voucherRepository.findById(id).get();
    User user = userRepository.findById(userId).get();

    Wallet wallet = walletRepository.save(Wallet.of(user, voucher));
    return walletMapper.toDto(wallet);
  }

  private void validateExistVoucherById(Long id) {
    boolean existsById = voucherRepository.existsById(id);

    if (!existsById) {
      throw new VoucherNotFoundException(MessageFormat.format("등록된 바우처를 찾을 수 없습니다. id : {0}", id));
    }
  }

  private void validateDuplicateVoucher(RequestVoucher request) {
    Optional<Voucher> saved = findDuplicatedVoucher(request);

    if (saved.isPresent()) {
      throw new DuplicateVoucherException("이미 중복된 바우처가 있습니다");
    }
  }

  private Optional<Voucher> findDuplicatedVoucher(RequestVoucher request) {
    return switch (request.getVoucherType()) {
      case FIXED -> voucherRepository.findByAmountAndMinLimit(request.getValue(),
          request.getMinLimit());
      case PERCENT -> voucherRepository.findByPercentAndMinLimit(request.getValue(),
          request.getMinLimit());
    };
  }

  private void validateExistUserById(Long userId) {
    boolean existsById = userRepository.existsById(userId);

    if (!existsById) {
      throw new EntityNotFoundException(
          MessageFormat.format("등록된 사용자를 찾을 수 없습니다. userId : {0}", userId));
    }
  }

  private void validateDuplicatedWallet(Long id, Long userId) {
    boolean existsByUserIdAndVoucherId = walletRepository.existsByUserIdAndVoucherId(userId, id);

    if (existsByUserIdAndVoucherId) {
      throw new DuplicateWalletException(
          MessageFormat.format(" 사용자(userId : {0})는 바우처(voucherId : {1})를 이미 갖고있습니다.", userId, id));
    }
  }

}
