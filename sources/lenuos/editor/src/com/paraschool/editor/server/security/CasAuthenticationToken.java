package com.paraschool.editor.server.security;

import java.security.Principal;

import org.apache.shiro.authc.AuthenticationToken;

@SuppressWarnings("serial")
public class CasAuthenticationToken implements AuthenticationToken {

	private final Principal principal;
	
	public CasAuthenticationToken(Principal principal) {
		this.principal = principal;
	}
	
	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

}
