package com.paraschool.editor.catalog.dao.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.dao.VirtualRepositoryDao;
import com.paraschool.editor.catalog.dao.jpa.util.EntityIdJpaDao;
import com.paraschool.editor.catalog.models.VirtualRepository;

/**
 * Define DAO for JPA VirtualRepository entity.  
 * @author blamouret
 *
 */
@Repository("virtualRepositoryDao")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class VirtualRepositoryJpaDao extends EntityIdJpaDao<VirtualRepository> implements VirtualRepositoryDao {
	
	/**
	 * Return the class associated with the dao.
	 * @return the class associated with the dao.
	 */
	@Override
	protected Class<VirtualRepository> getEntityClass() {
		return VirtualRepository.class;
	}
	
}
