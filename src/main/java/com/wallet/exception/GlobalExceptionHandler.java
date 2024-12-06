package com.wallet.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(WalletNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleWalletNotFound(WalletNotFoundException ex) {
		logger.warn("Wallet not found: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				ex.getErrorCode(),
				ex.getMessage(),
				"Проверьте правильность указанного идентификатора кошелька"
		);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException ex) {
		logger.warn("Insufficient funds: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				ex.getErrorCode(),
				ex.getMessage(),
				"Недостаточно средств для выполнения операции"
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidOperation(InvalidOperationException ex) {
		logger.warn("Invalid operation: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				ex.getErrorCode(),
				ex.getMessage(),
				"Проверьте правильность параметров операции"
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
		logger.warn("Invalid JSON received: {}", ex.getMessage());
		ErrorResponse error = new ErrorResponse(
				"INVALID_JSON",
				"Неверный формат JSON",
				"Проверьте правильность формата запроса"
		);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
		logger.error("Unexpected error occurred", ex);
		ErrorResponse error = new ErrorResponse(
				"INTERNAL_ERROR",
				"Внутренняя ошибка сервера",
				"Пожалуйста, попробуйте позже или обратитесь в службу поддержки"
		);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
