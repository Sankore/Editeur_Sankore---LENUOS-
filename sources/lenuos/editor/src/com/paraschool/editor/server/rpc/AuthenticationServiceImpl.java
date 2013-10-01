package com.paraschool.editor.server.rpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paraschool.commons.share.Author;
import com.paraschool.editor.client.rpc.AuthenticationService;
import com.paraschool.editor.server.User;
import com.paraschool.editor.server.UserProvider;
import com.paraschool.editor.server.config.CanRetrieveUserByEmailRealm;
import com.paraschool.editor.server.mail.LoginPasswordMailContext;
import com.paraschool.editor.server.mail.VelocityMailContext;
import com.paraschool.editor.shared.exception.MailException;
import com.paraschool.editor.shared.exception.UserNotFoundException;

@SuppressWarnings("serial")
@Singleton
public class AuthenticationServiceImpl extends CustomRemoteServiceServlet
		implements AuthenticationService {
	
	private MailService mailService;
	private UserProvider userProvider;
	
	private static final Log log = LogFactory.getLog(AuthenticationServiceImpl.class);

	@Inject
	public AuthenticationServiceImpl(MailService mailService, UserProvider userProvider) {
		super();
		this.mailService = mailService;
		this.userProvider = userProvider;
	}

	@Override
	public Author login(String username, String password, boolean remember) {

		Subject currentUser = SecurityUtils.getSubject();

		logger.debug("User [" + username + "] request authentication");
		
		if (!currentUser.isAuthenticated()) {
			logger.debug("User is not authenticated. Try to authenticate");
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			//FIXME Je n'ai pas réussi à invalider en administration les remenbered
			//token.setRememberMe(true);
			currentUser.login(token);
			token.clear();
		}
		logger.debug("Is user authenticated ?" + currentUser.isAuthenticated());
		User u = userProvider.getCurrentUser();
		return new Author(u.getAuthorName(), null, null, null);
	}

	@Override
	public void logout() {
		try{
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout();
		}catch (Throwable e) {}
	}

	@Override
	public void sendLogin(String email, String lang) {
		
		RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		UsernamePasswordToken token = null;
		for (Realm realm : rsm.getRealms()) {
			if (realm instanceof CanRetrieveUserByEmailRealm) {
				CanRetrieveUserByEmailRealm aRealm = (CanRetrieveUserByEmailRealm) realm;
				token = aRealm.retrieveForEmail(email);
				if(token != null)
					break;
			}
		}
		
		if (token == null) {
			throw new UserNotFoundException();
		}

		try {
			VelocityMailContext context = new LoginPasswordMailContext(
					token.getUsername(), 
					String.valueOf(token.getPassword()), 
					lang);
			this.mailService.sendMail(email, context);
		} catch (Exception e) {
			log.error("Cannot send mail", e);
			throw new MailException(e);
		}
	}
}
