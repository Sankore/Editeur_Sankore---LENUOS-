package com.paraschool.editor.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.paraschool.commons.share.Author;

public interface AuthenticationServiceAsync {

	void login(String username, String password, boolean remember, AsyncCallback<Author> callback);

	void logout(AsyncCallback<Void> callback);

	void sendLogin(String email, String lang, AsyncCallback<Void> callback);

}
