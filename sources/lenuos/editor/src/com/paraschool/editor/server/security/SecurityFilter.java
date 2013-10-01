package com.paraschool.editor.server.security;

import javax.sql.DataSource;

import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.servlet.IniShiroFilter;

import com.google.inject.Inject;

/*
 * Created at 24 sept. 2010
 * By bathily
 */
public class SecurityFilter extends IniShiroFilter {

	@Inject
	DataSource dataSource;
	
	@Override
	public void init() throws Exception {
		super.init();
		
		for (Realm realm : ((RealmSecurityManager)getSecurityManager()).getRealms()) {
			if (realm instanceof AppJdbcRealm) {
				AppJdbcRealm jdbcRealm = (AppJdbcRealm) realm;
				if(jdbcRealm.getDataSource() == null)
					jdbcRealm.setDataSource(dataSource);
			}
		}
	}
	
}