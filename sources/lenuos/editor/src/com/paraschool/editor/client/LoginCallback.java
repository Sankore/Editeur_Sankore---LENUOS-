package com.paraschool.editor.client;

/**
 * 
 * @author blamouret
 *
 */
public interface LoginCallback {
	public void login(String login, String password);
	public void sendLoginPass(String email);
}
