package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class UnauthorizedException extends AuthorizationException {

	public UnauthorizedException() {
		super();
	}

	public UnauthorizedException(String message) {
		super(message);
	}

}
