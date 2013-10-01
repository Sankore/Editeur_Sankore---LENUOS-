package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class DisabledAccountException extends AccountException {

	public DisabledAccountException() {
		super();
	}

	public DisabledAccountException(String message) {
		super(message);
	}

}
