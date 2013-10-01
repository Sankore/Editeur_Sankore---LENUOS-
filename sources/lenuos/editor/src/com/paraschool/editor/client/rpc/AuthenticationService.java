package com.paraschool.editor.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paraschool.commons.share.Author;
import com.paraschool.editor.shared.exception.MailException;
import com.paraschool.editor.shared.exception.UserNotFoundException;

@RemoteServiceRelativePath("authenticate")
public interface AuthenticationService extends RemoteService {

	Author login(String username, String password, boolean remember);
	void logout();
	void sendLogin(String email, String lang) throws UserNotFoundException, MailException; 
	
}
