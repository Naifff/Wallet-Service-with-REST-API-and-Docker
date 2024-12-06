package com.wallet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class Wallet {
	@Id
	@Column(name = "wallet_id")
	private UUID id;

	// Используем версионирование для оптимистичной блокировки
	@Version
	private Long version;

	@Column(name = "balance", nullable = false)
	private BigDecimal balance;

	public Wallet() {
		this.balance = BigDecimal.ZERO;
	}

	public Wallet(UUID id) {
		this.id = id;
		this.balance = BigDecimal.ZERO;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}