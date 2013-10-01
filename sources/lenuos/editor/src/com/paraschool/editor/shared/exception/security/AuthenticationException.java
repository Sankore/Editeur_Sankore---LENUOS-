package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class AuthenticationException extends ApplicationSecurityException {

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}

	
}
