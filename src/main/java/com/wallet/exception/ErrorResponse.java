package com.wallet.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ErrorResponse {
	private String errorCode;
	private String message;
	private String details;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	public ErrorResponse(String errorCode, String message, String details) {
		this.errorCode = errorCode;
		this.message = message;
		this.details = details;
		this.timestamp = LocalDateTime.now();
	}

	// Геттеры
	public String getErrorCode() { return errorCode; }
	public String getMessage() { return message; }
	public String getDetails() { return details; }
	public LocalDateTime getTimestamp() { return timestamp; }
}
