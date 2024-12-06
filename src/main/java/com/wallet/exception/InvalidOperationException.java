package com.wallet.exception;

public class InvalidOperationException extends WalletException {
	public InvalidOperationException(String message) {
		super("INVALID_OPERATION", message);
	}
}
