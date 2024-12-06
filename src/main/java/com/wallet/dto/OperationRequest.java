package com.wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OperationRequest {
	@NotNull(message = "ID кошелька обязателен")
	private UUID walletId;

	@NotNull(message = "Тип операции обязателен")
	private OperationType operationType;

	@NotNull(message = "Сумма операции обязательна")
	@Positive(message = "Сумма должна быть положительной")
	private BigDecimal amount;

	// Добавляем поле для отслеживания времени создания запроса
	private LocalDateTime timestamp;

	// Конструктор по умолчанию, который инициализирует timestamp
	public OperationRequest() {
		this.timestamp = LocalDateTime.now();
	}

	// Полный конструктор для всех полей
	public OperationRequest(UUID walletId, OperationType operationType, BigDecimal amount) {
		this.walletId = walletId;
		this.operationType = operationType;
		this.amount = amount;
		this.timestamp = LocalDateTime.now();
	}

	// Геттеры и сеттеры
	public UUID getWalletId() {
		return walletId;
	}

	public void setWalletId(UUID walletId) {
		this.walletId = walletId;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	// Сеттер для timestamp может быть полезен при тестировании
	// или если нужно установить определённое время
	protected void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "OperationRequest{" +
				"walletId=" + walletId +
				", operationType=" + operationType +
				", amount=" + amount +
				", timestamp=" + timestamp +
				'}';
	}
}