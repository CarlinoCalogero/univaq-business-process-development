package it.univaq.disim.bpd.publicbillpostingservice.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApiError {

	private HttpStatus status;
	private int statusCode;
	private String message;
	private String path; // request URI
	private String requestMethod; // HTTP method (GET, POST, etc.)

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;

	public ApiError() {
		this.timestamp = LocalDateTime.now();
	}

	public ApiError(HttpStatus status, String message, String path, String requestMethod) {
		this();
		this.status = status;
		this.statusCode = status.value();
		this.message = message;
		this.path = path;
		this.requestMethod = requestMethod;
	}

	// Getters and setters omitted for brevity
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
		this.statusCode = status.value();
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
