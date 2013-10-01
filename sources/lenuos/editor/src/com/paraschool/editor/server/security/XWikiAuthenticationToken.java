package com.paraschool.editor.server.security;

import org.apache.shiro.authc.AuthenticationToken;

/*
 * Created at 28 févr. 2012
 * By bathily
 */
public class XWikiAuthenticationToken implements AuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5781457550781586807L;
	private final XWikiAuthResponse response;
	
	public XWikiAuthenticationToken(XWikiAuthResponse response) {
		this.response = response;
	}
	
	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		return response.getUsername();
	}

}
