package com.paraschool.editor.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/*
 * Created at 1 août 2010
 * By bathily
 */
@Singleton
public class MonoUserProvider implements UserProvider {

	private User user;
	 
	@Inject
	private MonoUserProvider(@Named("default.user") String user,
			@Named("default.author.name") String authorName) {
		this.user = new User(user, authorName, null, null);
	}
	
	@Override
	public User getCurrentUser() {
		return user;
	}

}
