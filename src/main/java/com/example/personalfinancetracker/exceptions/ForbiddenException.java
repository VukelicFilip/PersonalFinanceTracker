package com.example.personalfinancetracker.exceptions;

public class ForbiddenException extends RuntimeException {
	
	private static final long serialVersionUID = -6416367832551890817L;
	
	public ForbiddenException(String message) {
		super( message );
	}

	public ForbiddenException() {
		super();
	}

	public ForbiddenException(String message, Throwable cause) {
		super( message, cause );
	}

	public ForbiddenException(Throwable cause) {
		super( cause );
	}
	
}
