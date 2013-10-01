package com.paraschool.editor.catalog.dao;

import java.util.List;

import com.paraschool.editor.catalog.dao.util.EntityDao;
import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.models.Resource;

/**
 * Define Resource dao interface.
 * @author blamouret
 *
 */
public interface ResourceDao extends EntityDao<Resource>{

	public List<Resource> search(final String searchTxt, final List<Filter> filters);
	public boolean isUsed(Filter entity);
}
