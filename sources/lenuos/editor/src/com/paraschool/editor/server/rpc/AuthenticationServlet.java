package com.paraschool.editor.server.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class AuthenticationServlet extends HttpServlet {

	protected Log logger = LogFactory.getLog(getClass());	
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		
		try{
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);
			token.clear();
			
			String url = request.getContextPath();
			if(currentUser.hasRole("admin"))
				url += "/status";
			response.sendRedirect(url);
			
		}catch (Exception e) {
			logger.debug(e.getMessage());
			request.setAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, e.getClass().getCanonicalName());
			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
		}

	}

	
}
