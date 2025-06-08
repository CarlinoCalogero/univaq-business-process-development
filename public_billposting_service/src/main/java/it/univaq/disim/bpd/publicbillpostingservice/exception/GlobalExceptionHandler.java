package it.univaq.disim.bpd.publicbillpostingservice.exception;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(),
				request.getMethod());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ApiError> handleInvalidRequest(InvalidRequestException ex, HttpServletRequest request) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(),
				request.getMethod());
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining(", "));
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, request.getRequestURI(),
				request.getMethod());
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(MismatchingMessageCorrelationException.class)
	public ResponseEntity<ApiError> handleMismatchingMessageCorrelationException(
			MismatchingMessageCorrelationException ex, HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Could not correlate message: " + ex.getMessage(),
				request.getRequestURI(), request.getMethod());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleJsonParseError(HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		Throwable rootCause = ex.getCause();
		String detailedMessage = "Malformed JSON request";
		if (rootCause instanceof JsonParseException) {
			detailedMessage = "JSON parse error: " + rootCause.getMessage();
		}

		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + detailedMessage,
				request.getRequestURI(), request.getMethod());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneralError(Exception ex, HttpServletRequest request) {
		ex.printStackTrace(); // Replace with proper logging
		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + ex.getMessage(),
				request.getRequestURI(), request.getMethod());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
