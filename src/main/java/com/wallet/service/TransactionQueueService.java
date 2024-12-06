package com.wallet.service;

import com.wallet.dto.OperationRequest;
import com.wallet.exception.InsufficientFundsException;
import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionQueueService {
	private static final Logger logger = LoggerFactory.getLogger(TransactionQueueService.class);

	private final Queue<OperationRequest> operationQueue;
	private final WalletRepository walletRepository;
	private final int BATCH_SIZE = 100;

	public TransactionQueueService(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
		this.operationQueue = new ConcurrentLinkedQueue<>();
	}

	public void enqueueOperation(OperationRequest request) {
		operationQueue.offer(request);
	}

	@Scheduled(fixedRate = 100) // Выполняется каждые 100мс
	@Transactional
	public void processBatch() {
		List<OperationRequest> batch = new ArrayList<>();
		OperationRequest request;

		// Собираем батч операций
		while ((request = operationQueue.poll()) != null && batch.size() < BATCH_SIZE) {
			batch.add(request);
		}

		if (batch.isEmpty()) {
			return;
		}

		// Группируем операции по кошелькам для оптимизации обработки
		Map<UUID, List<OperationRequest>> walletOperations = batch.stream()
				.collect(Collectors.groupingBy(OperationRequest::getWalletId));

		// Обрабатываем операции для каждого кошелька
		walletOperations.forEach((walletId, operations) -> {
			try {
				processWalletOperations(walletId, operations);
			} catch (Exception e) {
				logger.error("Error processing operations for wallet {}: {}", walletId, e.getMessage());
				// Возвращаем операции в очередь для повторной обработки
				operations.forEach(this::enqueueOperation);
			}
		});
	}

	private void processWalletOperations(UUID walletId, List<OperationRequest> operations) {
		walletRepository.findByIdWithLock(walletId).ifPresent(wallet -> {
			// Сортируем операции по времени для сохранения порядка
			operations.sort(Comparator.comparing(OperationRequest::getTimestamp));

			// Применяем операции последовательно
			for (OperationRequest operation : operations) {
				try {
					validateAndUpdateBalance(wallet, operation);
					logger.info("Successfully processed operation {} for wallet {}",
							operation.getOperationType(), walletId);
				} catch (Exception e) {
					logger.error("Failed to process operation for wallet {}: {}",
							walletId, e.getMessage());
					throw e;
				}
			}

			walletRepository.save(wallet);
		});
	}

	private void validateAndUpdateBalance(Wallet wallet, OperationRequest operation) {
		switch (operation.getOperationType()) {
			case DEPOSIT:
				wallet.setBalance(wallet.getBalance().add(operation.getAmount()));
				break;
			case WITHDRAW:
				if (wallet.getBalance().compareTo(operation.getAmount()) < 0) {
					throw new InsufficientFundsException(
							"Insufficient funds in wallet " + wallet.getId());
				}
				wallet.setBalance(wallet.getBalance().subtract(operation.getAmount()));
				break;
		}
	}
}