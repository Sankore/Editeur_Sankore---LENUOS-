package com.paraschool.editor.server.rpc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class StatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(StatusServlet.class);
	
	@Inject @Named("security.required") private Boolean securityEnabled;
	@Inject @Named("application.version") private String applicationVersion;
	@Inject @Named("application.build") private String applicationBuild;
	@Inject @Named("application.buildDate") private String applicationBuildDate;

	@Inject
	private StatusServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		VelocityContext context = new VelocityContext();
		context.put("security", securityEnabled);
		context.put("version", applicationVersion);
		context.put("build", applicationBuild);
		context.put("buildDate", applicationBuildDate);
		
		if(securityEnabled) {
			SessionDAO sessionDAO = ((DefaultSessionManager)((DefaultWebSecurityManager)SecurityUtils.getSecurityManager()).getSessionManager()).getSessionDAO();
			Collection<Session> sessions = sessionDAO.getActiveSessions();
			ArrayList<Subject> subjects = new ArrayList<Subject>(sessions.size());
			
			for(Session session : sessions) {
				logger.debug(session);
				Subject.Builder subjectBuilder = new Subject.Builder(SecurityUtils.getSecurityManager());
				subjectBuilder.session(session);
				Subject subject = subjectBuilder.buildSubject();
				subjects.add(subject);
			}
			
			context.put("subjects", subjects);
		}
		
		try {
			Template view = Velocity.getTemplate("status.vm");
			StringWriter st = new StringWriter();
			view.merge(context, st);
			response.setContentType("text/html");
			response.getWriter().write(st.toString());
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
