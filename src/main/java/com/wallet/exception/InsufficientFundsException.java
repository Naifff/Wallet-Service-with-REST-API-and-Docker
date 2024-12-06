package com.wallet.exception;

public class InsufficientFundsException extends WalletException {
	public InsufficientFundsException(String message) {
		super("INSUFFICIENT_FUNDS", message);
	}
}
