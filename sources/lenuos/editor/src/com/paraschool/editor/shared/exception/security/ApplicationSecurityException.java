package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class ApplicationSecurityException extends RuntimeException {

	public ApplicationSecurityException() {
		super();
	}

	public ApplicationSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationSecurityException(String message) {
		super(message);
	}

	public ApplicationSecurityException(Throwable cause) {
		super(cause);
	}
	
}
