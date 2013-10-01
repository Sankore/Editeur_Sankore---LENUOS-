package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class UnauthenticatedException extends AuthorizationException {

	public UnauthenticatedException() {
		super();
	}

	public UnauthenticatedException(String message) {
		super(message);
	}

}
