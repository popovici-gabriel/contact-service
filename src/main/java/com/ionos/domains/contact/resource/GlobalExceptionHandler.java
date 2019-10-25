package com.ionos.domains.contact.resource;

import static com.ionos.domains.contact.resource.ErrorCode.BAD_REQUEST;
import static com.ionos.domains.contact.resource.ErrorCode.JOB_NOT_FOUND;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.oneandone.domain.regsys.model.generic.Error;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity resourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
		// @formatter:off
		return new ResponseEntity<>(
				new Error.Builder().code(JOB_NOT_FOUND.name()).message(exception.getMessage()).build(),
				HttpStatus.NOT_FOUND);
		// @formatter:on
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity resourceNotFoundException(EntityNotFoundException exception, WebRequest request) {
		// @formatter:off
		return new ResponseEntity<>(
				new Error.Builder().code(JOB_NOT_FOUND.name()).message(exception.getMessage()).build(),
				HttpStatus.NOT_FOUND);
		// @formatter:on
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity handleIllegalArgumentException(IllegalArgumentException exception, WebRequest webRequest) {
		// @formatter:off
		return ResponseEntity.badRequest()
				.body(new Error.Builder().code(BAD_REQUEST.name()).message("Bad request").build());
		// @formatter:on
	}

	@ExceptionHandler
	public ResponseEntity handleException(MethodArgumentNotValidException exception) {
		// @formatter:off
		return ResponseEntity.badRequest()
				.body(new Error.Builder().code(BAD_REQUEST.name()).message(exception.getMessage()).build());
		// @formatter:on
	}
}
