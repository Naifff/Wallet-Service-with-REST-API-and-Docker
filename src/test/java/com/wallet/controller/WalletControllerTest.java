package com.wallet.controller;

import com.wallet.dto.OperationRequest;
import com.wallet.dto.OperationType;
import com.wallet.model.Wallet;
import com.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WalletService walletService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testSuccessfulDeposit() throws Exception {
		UUID walletId = UUID.randomUUID();
		OperationRequest request = new OperationRequest();
		request.setWalletId(walletId);
		request.setOperationType(OperationType.DEPOSIT);
		request.setAmount(new BigDecimal("100.00"));

		Wallet wallet = new Wallet(walletId);
		wallet.setBalance(new BigDecimal("100.00"));

		when(walletService.processOperation(any())).thenReturn(wallet);

		mockMvc.perform(post("/api/v1/wallet")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value("100.0"));
	}

	@Test
	public void testGetWalletBalance() throws Exception {
		UUID walletId = UUID.randomUUID();
		Wallet wallet = new Wallet(walletId);
		wallet.setBalance(new BigDecimal("100.00"));

		when(walletService.getWallet(walletId)).thenReturn(wallet);

		mockMvc.perform(get("/api/v1/wallets/" + walletId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance").value("100.0"));
	}

	@Test
	public void testWalletNotFound() throws Exception {
		UUID walletId = UUID.randomUUID();
		when(walletService.getWallet(walletId))
				.thenThrow(new RuntimeException("Wallet not found"));

		mockMvc.perform(get("/api/v1/wallets/" + walletId))
				.andExpect(status().isNotFound());
	}
}