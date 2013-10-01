package com.paraschool.editor.server.config;

import org.apache.shiro.authc.UsernamePasswordToken;

/*
 * Created at 25 sept. 2010
 * By bathily
 */
public interface CanRetrieveUserByEmailRealm {
	UsernamePasswordToken retrieveForEmail(String email);
}
