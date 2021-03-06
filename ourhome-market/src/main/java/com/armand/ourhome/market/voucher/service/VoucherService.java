package com.armand.ourhome.market.voucher.service;

import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.common.error.exception.user.UserNotFoundException;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.voucher.converter.VoucherConverter;
import com.armand.ourhome.market.voucher.converter.WalletMapper;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.domain.Wallet;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.VoucherType;
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

		return new PageResponse<>(pages.getContent(), pages.getNumber(), pages.getTotalPages(),
			pages.getNumberOfElements(), pages.getTotalElements());
	}

	@Transactional
	public VoucherDto save(RequestVoucher request) {
		validateDuplicateVoucher(request.getValue(), request.getMinLimit(),
			request.getVoucherType());

		Voucher voucher = voucherConverter.toEntity(request);
		voucherRepository.save(voucher);
		return voucherConverter.toDto(voucher);
	}

	@Transactional
	public VoucherDto update(Long id, RequestVoucher request) {
		// ?????? id?????? ???????????? ??????????????? ??????
		Voucher voucher = voucherRepository.findById(id)
			.orElseThrow(() -> new VoucherNotFoundException(
				MessageFormat.format("????????? ???????????? ?????? ??? ????????????. id : {0}", id)));
		// ????????????????????? ????????? ?????? ???????????? ??????????????? ??????
		validateDuplicateVoucher(request.getValue(), request.getMinLimit(),
			request.getVoucherType());

		voucher.update(request.getValue(), request.getMinLimit());
		voucherRepository.flush(); // updatedAt ??????
		return voucherConverter.toDto(voucher);
	}

	@Transactional
	public void delete(Long id) {
		Voucher voucher = voucherRepository.findById(id)
			.orElseThrow(() -> new VoucherNotFoundException(
				MessageFormat.format("????????? ???????????? ?????? ??? ????????????. id : {0}", id)));

		voucherRepository.delete(voucher);
	}

	@Transactional
	public WalletDto assignToUser(Long id, Long userId) {
		Voucher voucher = voucherRepository.findById(id)
			.orElseThrow(() -> new VoucherNotFoundException(
				MessageFormat.format("????????? ???????????? ?????? ??? ????????????. id : {0}", id)));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));
		validateDuplicatedWallet(id, userId);

		Wallet wallet = walletRepository.save(Wallet.of(user, voucher));
		return walletMapper.toDto(wallet);
	}

	private void validateDuplicateVoucher(int value, int minLimit, VoucherType voucherType) {
		Optional<Voucher> saved = findDuplicatedVoucher(value, minLimit, voucherType);

		if (saved.isPresent()) {
			throw new DuplicateVoucherException("?????? ????????? ???????????? ????????????");
		}
	}

	private Optional<Voucher> findDuplicatedVoucher(int value, int minLimit,
		VoucherType voucherType) {
		return switch (voucherType) {
			case FIXED -> voucherRepository.findByAmountAndMinLimit(value, minLimit);
			case PERCENT -> voucherRepository.findByPercentAndMinLimit(value, minLimit);
		};
	}

	private void validateDuplicatedWallet(Long id, Long userId) {
		boolean existsByUserIdAndVoucherId = walletRepository.existsByUserIdAndVoucherId(userId,
			id);

		if (existsByUserIdAndVoucherId) {
			throw new DuplicateWalletException(
				MessageFormat.format(" ?????????(userId : {0})??? ?????????(voucherId : {1})??? ?????? ??????????????????.", userId,
					id));
		}
	}

}
