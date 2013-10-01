package com.paraschool.editor.catalog.dao.jpa.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.dao.util.EntityDao;
import com.paraschool.editor.catalog.models.util.EntityId;
import com.paraschool.editor.catalog.models.util.IEntity;

/**
 * Define generic DAO for JPA entity.
 * 
 * @author blamouret
 * 
 * @param <E>
 *            the entity to persist
 * @param <PK>
 *            the entity primary key.
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public abstract class EntityJpaDao<E extends IEntity, PK> implements
		EntityDao<E> {

	@PersistenceContext
	protected EntityManager em;

	/**
	 * Return the class associated with the dao.
	 * 
	 * @return the class associated with the dao.
	 */
	protected abstract Class<E> getEntityClass();

	/**
	 * Return one object according to a primary key.
	 * 
	 * @param pk
	 *            the primary key to find object.
	 * @return the object associated with the primary key.
	 */
	public E find(PK pk) {
		return em.find(this.getEntityClass(), pk);
	}

	/**
	 * Return all object from the persistent storage.
	 */
	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		String query = "SELECT o FROM " + this.getEntityClass().getName()
				+ " o ";
		return (List<E>) em.createQuery(query).getResultList();
	}

	/**
	 * Create a new object in the persistent storage.
	 * 
	 * @param entity
	 *            the entity to create.
	 */
	@Transactional(readOnly = false)
	private void create(E entity) {
		em.persist(entity);
	}

	/**
	 * Update an existed object in the persistent storage.
	 * 
	 * @param entity
	 *            the entity to update.
	 */
	@Transactional(readOnly = false)
	private E update(E entity) {
		return em.merge(entity);
	}

	/**
	 * Return true is the object is not persisted yet.
	 * 
	 * @param entity
	 *            the entity.
	 * @return true is the object is not persisted yet.
	 */
	protected abstract boolean isNew(E entity);

	/**
	 * Save an object in the persistent storage. According to the isNew result,
	 * the entity will be created or updated.
	 */
	@Transactional(readOnly = false)
	public E save(E entity) {
		if (this.isNew(entity)) {
			this.create(entity);
			return entity;
		} else {
			System.out.println("Updating " + entity.getClass().getSimpleName()
					+ " " + ((EntityId) entity).getId());
			return this.update(entity);
		}
	}

	/**
	 * Delete an existinf object in the persistent storage.
	 */
	@Transactional(readOnly = false)
	public void delete(E entity) {
		em.remove(entity);
	}

	/**
	 * Return the number of element in the persistent storage.
	 */
	public Long count() {
		return em.createQuery(
				"select count(o) from " + this.getEntityClass().getName()
						+ " o", Long.class).getSingleResult();
	}

	protected FullTextEntityManager getFullTextEntityManager() {
		return Search.getFullTextEntityManager(this.em);
	}

	protected void indexAll() {
		this.purgeIndex();
		FullTextEntityManager ftem = this.getFullTextEntityManager();
		String query = "SELECT o FROM " + this.getEntityClass().getName()
						+ " o ";
		List<E> result = ftem.createQuery(query,this.getEntityClass()).getResultList();
		for (E o : result) {
			ftem.index(o);
		}
	}
	
	protected void purgeIndex() {
		FullTextEntityManager ftem = this.getFullTextEntityManager();
		ftem.purgeAll(this.getEntityClass());
	}

}
