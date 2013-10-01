package com.paraschool.editor.shared.exception;

/*
 * Created at 23 sept. 2010
 * By Bruno Lamouret
 */
@SuppressWarnings("serial")
public class MailException extends RuntimeException {
	
	public MailException() {
		super();
	}

	public MailException(String message) {
		super(message);
	}

	public MailException(Throwable cause) {
		super(cause);
	}

	public MailException(String message, Throwable cause) {
		super(message, cause);
	}
}
