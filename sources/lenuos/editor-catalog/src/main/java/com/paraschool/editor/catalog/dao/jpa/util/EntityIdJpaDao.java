package com.paraschool.editor.catalog.dao.jpa.util;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.models.util.EntityId;

/**
 * Define generic DAO for JPA entity with integer as primary key.  
 * @author blamouret
 *
 * @param <E> the entity to persist
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public abstract class EntityIdJpaDao<E extends EntityId> extends EntityJpaDao<E,Integer> {
	
	/**
	 * Return true is the object is not persisted yet.
	 * The object is considered not persisted when primary key is null.
	 * @param entity the entity.
	 * @return true is the object is not persisted yet.
	 */
	protected boolean isNew(E entity) {
		return entity.getId() == null;
	}

}
