package com.example.personalfinancetracker.exception_handling;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = -6416367832551890817L;
	
	public ValidationException(String message) {
		super( message );
	}

	public ValidationException() {
		super();
	}

	public ValidationException(String message, Throwable cause) {
		super( message, cause );
	}

	public ValidationException(Throwable cause) {
		super( cause );
	}
	
}
