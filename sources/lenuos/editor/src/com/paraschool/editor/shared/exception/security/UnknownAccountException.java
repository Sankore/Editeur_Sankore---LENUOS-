package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class UnknownAccountException extends AccountException {

	public UnknownAccountException() {
		super();
	}

	public UnknownAccountException(String message) {
		super(message);
	}

}
