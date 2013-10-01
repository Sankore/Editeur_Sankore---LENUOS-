package com.paraschool.editor.server;

import com.paraschool.editor.shared.exception.MissingUserException;

/*
 * Created at 1 août 2010
 * By bathily
 */
public interface UserProvider {
	User getCurrentUser() throws MissingUserException;
}
