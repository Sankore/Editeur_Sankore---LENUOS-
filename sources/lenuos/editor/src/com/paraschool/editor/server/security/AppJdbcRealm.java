package com.paraschool.editor.server.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;

import com.paraschool.editor.server.config.CanRetrieveUserByEmailRealm;

/**
 * Realm that allows authentication and authorization via JDBC calls.
 * {@see #JdbcRealm(org.apache.shiro.realm.jdbc.JdbcRealm)}
 * It defines a new query for getting login/password according to an email. 
 * 
 * @author Bruno Lamouret
 */
public class AppJdbcRealm extends JdbcRealm implements CanRetrieveUserByEmailRealm {

	private static final Log log = LogFactory.getLog(AppJdbcRealm.class);

	private String retrieveEmailQuery = null;

	public AppJdbcRealm() {
		super();
	}

	public void setRetrieveEmailQuery(String retrieveEmailQuery) {
		this.retrieveEmailQuery = retrieveEmailQuery;
	}

	public UsernamePasswordToken retrieveForEmail(String email) {
		if (email == null || email.trim().length() == 0) {
			throw new AuthorizationException("email cannot be null.");
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return this.getUser(conn, email);

		} catch (SQLException e) {
			final String message = "There was a SQL error while searching user for email ["
					+ email + "]";
			if (log.isErrorEnabled()) {
				log.error(message, e);
			}

			// Rethrow any SQL errors as an authorization exception
			throw new AuthorizationException(message, e);
		} finally {
			JdbcUtils.closeConnection(conn);
		}
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	private UsernamePasswordToken getUser(Connection conn, String email)
			throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		UsernamePasswordToken token = null;

		try {
			ps = conn.prepareStatement(this.retrieveEmailQuery);
			ps.setString(1, email);

			// Execute query
			rs = ps.executeQuery();

			// Loop over results - although we are only expecting one result,
			// since usernames should be unique
			boolean foundResult = false;
			while (rs.next()) {

				// Check to ensure only one row is processed
				if (foundResult) {
					throw new AuthenticationException(
							"More than one user row found for email [" + email
									+ "]. Emails must be unique.");
				}

				token = new UsernamePasswordToken(rs.getString(1), rs.getString(2));
				foundResult = true;
			}
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}

		return token;
	}

}
