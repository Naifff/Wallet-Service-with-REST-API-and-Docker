package com.wallet.exception;

public abstract class WalletException extends RuntimeException {
	private final String errorCode;

	protected WalletException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
