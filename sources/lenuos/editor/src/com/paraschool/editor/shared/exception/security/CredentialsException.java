package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class CredentialsException extends AuthenticationException {

	public CredentialsException() {
		super();
	}

	public CredentialsException(String message) {
		super(message);
	}

}
