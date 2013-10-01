package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class ExcessiveAttemptsException extends AccountException {

	public ExcessiveAttemptsException() {
		super();
	}

	public ExcessiveAttemptsException(String message) {
		super(message);
	}

}
