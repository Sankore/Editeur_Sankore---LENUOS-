package com.paraschool.editor.server;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.paraschool.editor.shared.exception.MissingUserException;

/*
 * Created at 3 août 2010
 * By bathily
 */
public class SubjectUserProvider implements UserProvider {

	@Override
	public User getCurrentUser() throws MissingUserException {
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser == null || currentUser.getPrincipal() == null) throw new MissingUserException();
		String username = currentUser.getPrincipal().toString();
		return new User(username, username, null, null);
	}

}
