package com.wallet.controller;

import com.wallet.dto.OperationRequest;
import com.wallet.exception.InsufficientFundsException;
import com.wallet.model.Wallet;
import com.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
	private final WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@PostMapping("/wallet")
	public ResponseEntity<?> processOperation(@RequestBody OperationRequest request) {
		try {
			Wallet wallet = walletService.processOperation(request);
			return ResponseEntity.ok(wallet);
		} catch (InsufficientFundsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/wallets/{walletId}")
	public ResponseEntity<?> getWallet(@PathVariable UUID walletId) {
		try {
			Wallet wallet = walletService.getWallet(walletId);
			return ResponseEntity.ok(wallet);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}