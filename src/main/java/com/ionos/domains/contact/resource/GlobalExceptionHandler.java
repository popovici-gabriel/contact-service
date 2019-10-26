package com.ionos.domains.contact.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity resourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
		// @formatter:off
		return ResponseEntity.notFound().build();
		// @formatter:on
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity resourceNotFoundException(EntityNotFoundException exception, WebRequest request) {
		// @formatter:off
		return ResponseEntity.notFound().build();
		// @formatter:on
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity handleIllegalArgumentException(IllegalArgumentException exception, WebRequest webRequest) {
		// @formatter:off
		return ResponseEntity.badRequest().build();
		// @formatter:on
	}

	@ExceptionHandler
	public ResponseEntity handleException(MethodArgumentNotValidException exception) {
		// @formatter:off
		return  ResponseEntity.badRequest().build();
		// @formatter:on
	}
}
