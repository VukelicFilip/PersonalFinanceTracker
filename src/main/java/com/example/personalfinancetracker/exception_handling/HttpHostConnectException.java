package com.example.personalfinancetracker.exception_handling;

public class HttpHostConnectException extends RuntimeException {
	
	private static final long serialVersionUID = -6416367832551890817L;
	
	public HttpHostConnectException(String message) {
		super( message );
	}

	public HttpHostConnectException() {
		super();
	}

	public HttpHostConnectException(String message, Throwable cause) {
		super( message, cause );
	}

	public HttpHostConnectException(Throwable cause) {
		super( cause );
	}
	
}
