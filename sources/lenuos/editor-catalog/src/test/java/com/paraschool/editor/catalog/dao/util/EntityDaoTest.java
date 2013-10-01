package com.paraschool.editor.catalog.dao.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.paraschool.editor.catalog.models.util.EntityId;

/**
 * Define generic JUnit test for a dao
 * @author blamouret
 *
 * @param <E> the entity class to test.
 */
public abstract class EntityDaoTest<E extends EntityId> {
	
	/**
	 * Return the dao associated with the tested entity.
	 * @return
	 */
	protected abstract EntityDao<E> getDAO();

	/**
	 * Create a new non persisted instance of the entity.
	 * @return a new instance.
	 */
	protected abstract E init();

	/**
	 * Modify an entity.
	 * The modifiied entity is not persisted after calling this method.
	 * @param entity to modify.
	 */
	protected abstract void modify(E entity);

	/**
	 * Test if 2 entity are equals.
	 * @param one the firt entity to test
	 * @param two the second entity to test.
	 */
	protected abstract void assertEquals(E one, E two);

	/**
	 * Define the persistence context.
	 */
	@PersistenceContext
	protected EntityManager em;

	/**
	 * JUnit validation after persisting a new instance.
	 * @param toCreate the entity before persisting
	 * @param saved the entity after persisted.
	 */
	private void assertCreated(E toCreate, E saved) {
		assert toCreate.getId() == null;
		assert saved.getId() != null;
		assert saved.getVersion() == 0;
		this.assertEquals(toCreate, saved);
	}

	/**
	 * JUnit validation after persisting an existed instance.
	 * @param toUpdate the entity before persisting
	 * @param saved the entity after persisted.
	 */
	protected void assertUpdated(E toUpdate, E saved) {
		assert toUpdate.getId() != null;
		assert saved.getId() != null;
		assert saved.getId().equals(toUpdate.getId());
		this.assertEquals(toUpdate, saved);
	}

	/**
	 * JUnit validation after each persisted method (create, update, remove)
	 * @param entity the entity persisted.
	 */
	protected void assertEntity(E entity) {
		// Nothing by default
	}

	/**
	 * JUnit test for CRUD method.
	 */
	protected void crud() {
		E entity = null;

		// Create
		entity = this.create();
		this.assertEntity(entity);

		// Read
		this.find(entity);
		this.assertEntity(entity);

		// Update
		this.update(entity);
		this.assertEntity(entity);

		// Delete
		this.delete(entity);

	}

	/**
	 * Test findAll method.
	 */
	protected void findAll() {
		List<E> list = this.getDAO().findAll();
		int size = list.size() + 1;
		E entity = this.create();
		list = this.getDAO().findAll();
		assert size == list.size();
		this.delete(entity);
	}

	/**
	 * Test count method.
	 */
	protected void count() {
		List<E> list = this.getDAO().findAll();
		E entity = this.create();
		list = this.getDAO().findAll();
		int size = list.size();
		Long count = this.getDAO().count();
		assert size == count;
		this.delete(entity);
	}

	/**
	 * Clean and flush persistence context.
	 */
	private void clean() {
		em.flush();
		em.clear();
	}

	/**
	 * JUnit test for Created CRUD method.
	 */
	protected E create() {
		this.clean();
		E entity = this.init();
		this.getDAO().save(entity);
		this.clean();
		assert entity.getId() != null;
		E find = this.find(entity.getId(), true);
		this.clean();
		this.assertCreated(this.init(), find);
		this.clean();
		return find;
	}

	/**
	 * JUnit test for Updated CRUD method.
	 */
	protected void update(E entity) {
		this.clean();
		this.modify(entity);
		Integer version = entity.getVersion();
		this.getDAO().save(entity);
		this.clean();
		E find = this.find(entity.getId(), true);
		this.assertUpdated(entity, find);
		assert version + 1 == find.getVersion();
		this.clean();
	}

	/**
	 * JUnit test for Readed CRUD method.
	 */
	protected E find(E entity) {
		this.clean();
		E e = this.find(entity.getId(), true);
		this.clean();
		return e;
	}

	/**
	 * JUnit test for Readed CRUD method for existed on unexisted entity.
	 * 
	 */
	protected E find(Integer id, boolean existed) {
		E saved = this.getDAO().find(id);
		if (existed) {
			assert null != saved;
		} else {
			assert null == saved;
		}
		return saved;
	}

	/**
	 * JUnit test for Deleted CRUD method.
	 */
	protected void delete(E entity) {
		this.clean();
		E saved = this.find(entity.getId(), true);
		Integer id = entity.getId();
		this.getDAO().delete(saved);
		this.clean();
		this.find(id, false);
		this.clean();
	}
	
}
