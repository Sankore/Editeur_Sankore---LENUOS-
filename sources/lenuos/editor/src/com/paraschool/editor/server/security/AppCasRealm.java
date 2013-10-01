package com.paraschool.editor.server.security;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Realm that allows authentication and authorization via CAS server.
 * 
 * @author Bruno Lamouret
 */
public class AppCasRealm extends AuthorizingRealm {

	final AllowAllCredentialsMatcher credentialsMatcher = new AllowAllCredentialsMatcher();
	
	/**
	 * Default Constructor
	 */
	public AppCasRealm() {
		super();
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// null usernames are invalid
		if (principals == null) {
			throw new AuthorizationException(
					"PrincipalCollection method argument cannot be null.");
		}
		Set<String> roleNames = null;
		Set<String> permissions = null;

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		CasAuthenticationToken upToken = (CasAuthenticationToken) token;

		return new SimpleAccount(upToken.getPrincipal(), upToken.getCredentials(),
				super.getName());
	}

	@Override
	public CredentialsMatcher getCredentialsMatcher() {
		return credentialsMatcher;
	}
	
	
}
