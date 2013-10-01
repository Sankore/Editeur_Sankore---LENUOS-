package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class AccountException extends AuthenticationException {

	public AccountException() {
		super();
	}

	public AccountException(String message) {
		super(message);
	}

}
