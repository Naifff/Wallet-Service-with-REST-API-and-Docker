package com.wallet.service;

import com.wallet.dto.OperationRequest;
import com.wallet.dto.OperationType;
import com.wallet.exception.InsufficientFundsException;
import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {
	private final WalletRepository walletRepository;

	public WalletService(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	// Метод для получения баланса кошелька (только чтение)
	@Transactional(readOnly = true)
	public Wallet getWallet(UUID id) {
		return walletRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Кошелек не найден"));
	}

	// Метод для обработки операций с кошельком
	@Transactional
	public Wallet processOperation(OperationRequest request) {
		// Получаем кошелек с блокировкой
		Wallet wallet = walletRepository.findByIdWithLock(request.getWalletId())
				.orElseGet(() -> new Wallet(request.getWalletId()));

		if (request.getOperationType() == OperationType.DEPOSIT) {
			wallet.setBalance(wallet.getBalance().add(request.getAmount()));
		} else {
			if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
				throw new InsufficientFundsException("Недостаточно средств");
			}
			wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
		}

		return walletRepository.save(wallet);
	}
}