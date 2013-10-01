package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class UnsupportedTokenException extends AuthenticationException {

	public UnsupportedTokenException() {
		super();
	}

	public UnsupportedTokenException(String message) {
		super(message);
	}

}
