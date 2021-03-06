package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class AuthorizationException extends ApplicationSecurityException {

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}

	
}
