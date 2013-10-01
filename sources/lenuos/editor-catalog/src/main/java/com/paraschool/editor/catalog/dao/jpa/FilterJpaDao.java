package com.paraschool.editor.catalog.dao.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.dao.FilterDao;
import com.paraschool.editor.catalog.dao.jpa.util.EntityIdJpaDao;
import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.models.Filter;

/**
 * Define DAO for JPA Filter entity.  
 * @author blamouret
 *
 */
@Repository("filterDao")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FilterJpaDao extends EntityIdJpaDao<Filter> implements FilterDao {
	
	private static final String COUNT_BY_FAMILY = "SELECT count(o) FROM "
		+ Filter.class.getName() + " o where o.family.id = :id";

	/**
	 * Return the class associated with the dao.
	 * @return the class associated with the dao.
	 */
	@Override
	protected Class<Filter> getEntityClass() {
		return Filter.class;
	}
	
	/**
	 * Return true if the family filter is used on filters.
	 * @param entity the family to test.
	 * @return true if the family filter is used on filters.
	 */
	public boolean isUsed(FamilyFilter entity) {
		Long count = em.createQuery(COUNT_BY_FAMILY, Long.class)
		.setParameter("id", entity.getId()).getSingleResult();
		return count != null && count > 0;
	}

}
