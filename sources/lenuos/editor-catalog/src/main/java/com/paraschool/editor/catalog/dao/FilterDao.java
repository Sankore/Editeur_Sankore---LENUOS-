package com.paraschool.editor.catalog.dao;

import com.paraschool.editor.catalog.dao.util.EntityDao;
import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.models.Filter;

/**
 * Define Filter dao interface.
 * @author blamouret
 *
 */
public interface FilterDao extends EntityDao<Filter>{

	public boolean isUsed(FamilyFilter entity);

}
