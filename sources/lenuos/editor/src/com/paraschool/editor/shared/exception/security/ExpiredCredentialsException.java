package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class ExpiredCredentialsException extends CredentialsException {

	public ExpiredCredentialsException() {
		super();
	}

	public ExpiredCredentialsException(String message) {
		super(message);
	}

}
