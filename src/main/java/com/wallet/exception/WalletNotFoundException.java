package com.wallet.exception;

public class WalletNotFoundException extends WalletException {
	public WalletNotFoundException(String message) {
		super("WALLET_NOT_FOUND", message);
	}
}