package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class IncorrectCredentialsException extends CredentialsException {

	public IncorrectCredentialsException() {
		super();
	}

	public IncorrectCredentialsException(String message) {
		super(message);
	}

}
