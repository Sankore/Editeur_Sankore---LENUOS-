package com.paraschool.editor.catalog.dao.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.dao.FamilyFilterDao;
import com.paraschool.editor.catalog.dao.jpa.util.EntityIdJpaDao;
import com.paraschool.editor.catalog.models.FamilyFilter;

/**
 * Define DAO for JPA FamilyFilter entity.  
 * @author blamouret
 *
 */
@Repository("familyFilterDao")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FamilyFilterJpaDao extends EntityIdJpaDao<FamilyFilter> implements FamilyFilterDao {
	
	/**
	 * Return the class associated with the dao.
	 * @return the class associated with the dao.
	 */
	@Override
	protected Class<FamilyFilter> getEntityClass() {
		return FamilyFilter.class;
	}

}
