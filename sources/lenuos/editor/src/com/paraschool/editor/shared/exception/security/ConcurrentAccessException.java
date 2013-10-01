package com.paraschool.editor.shared.exception.security;

@SuppressWarnings("serial")
public class ConcurrentAccessException extends AccountException {

	public ConcurrentAccessException() {
		super();
	}

	public ConcurrentAccessException(String message) {
		super(message);
	}

}
