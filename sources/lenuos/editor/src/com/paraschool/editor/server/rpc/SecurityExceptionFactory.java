package com.paraschool.editor.server.rpc;

import org.apache.shiro.ShiroException;

import com.paraschool.editor.shared.exception.security.ApplicationSecurityException;


public class SecurityExceptionFactory {

	public static ApplicationSecurityException get(ShiroException shiroException) {
		
		try {
			@SuppressWarnings("unchecked")
			Class<? extends ApplicationSecurityException> aclass = (Class<? extends ApplicationSecurityException>) Class.forName(ApplicationSecurityException.class.getPackage().getName()+"."+shiroException.getClass().getSimpleName());
			return aclass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return new ApplicationSecurityException();
	}
}
