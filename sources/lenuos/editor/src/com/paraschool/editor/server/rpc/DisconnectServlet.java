package com.paraschool.editor.server.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Servlet implementation class DisconnectServlet
 */
@Singleton
public class DisconnectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Log logger = LogFactory.getLog(DisconnectServlet.class);
    
    @Inject @Named("security.required") private Boolean securityEnabled;
    
	@Inject
    private DisconnectServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if(securityEnabled && id != null && id.trim().length() != 0) {
			logger.debug("Disconnect user with session id "+id);
			Subject.Builder builder = new WebSubject.Builder(SecurityUtils.getSecurityManager(), request, response);
			builder.sessionId(id);
			Subject subject = builder.buildSubject();
			subject.logout();	
		}
		response.sendRedirect(request.getContextPath()+"/status");
	}

}
