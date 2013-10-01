package com.paraschool.editor.client.event.app;

import com.paraschool.commons.share.Author;

/*
 * Created at 25 oct. 2010
 * By bathily
 */
public class AuthenticatedEvent extends AuthenticationEvent {

	private final Author author;
	
	public AuthenticatedEvent(Author author) {
		super();
		this.author = author;
	}

	public Author getAuthor() {
		return author;
	}

	@Override
	protected void dispatch(AuthenticationEventHandler handler) {
		handler.onAuthenticated(this);
	}

}
