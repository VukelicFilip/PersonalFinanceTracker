package com.example.personalfinancetracker.exception_handling;

public class NotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -6416367832551890817L;
	
	public NotFoundException(String message) {
		super( message );
	}

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message, Throwable cause) {
		super( message, cause );
	}

	public NotFoundException(Throwable cause) {
		super( cause );
	}

}
