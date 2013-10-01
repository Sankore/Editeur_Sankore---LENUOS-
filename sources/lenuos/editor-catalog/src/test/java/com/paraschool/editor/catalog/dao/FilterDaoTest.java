package com.paraschool.editor.catalog.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.dao.util.EntityDao;
import com.paraschool.editor.catalog.dao.util.EntityDaoTest;
import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.models.Filter;

/**
 * Define JUnit test for a Filter entity.
 * @author blamouret
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/paraschool/editor/catalog/*.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class FilterDaoTest extends EntityDaoTest<Filter> {

	/**
	 * Filter DAO
	 */
	@Autowired
	FilterDao filterDao;
	
	/**
	 * Family filter DAO
	 */
	@Autowired
	FamilyFilterDao familyDao;
	
	/**
	 * Family filter created for this test.
	 */
	FamilyFilter familyFilter;
	
	/**
	 * JUnit test before executed one test.
	 */
	@Before
	public void before() {
		assert this.getDAO() != null;
		assert this.familyDao != null;

		// Create one family
		this.familyFilter = new FamilyFilter();
		this.familyFilter.setName("Family");
		this.familyDao.save(this.familyFilter);
		
	}

	/**
	 * JUnit test after executed one test.
	 */
	@After
	public void after() {
		// Remove created family.
		this.familyFilter = this.familyDao.find(this.familyFilter.getId());
		this.familyDao.delete(this.familyFilter);
		this.familyFilter = null;
	}

	@Override
	protected EntityDao<Filter> getDAO() {
		return filterDao;
	}

	@Override
	protected Filter init() {
		Filter entity = new Filter();
		entity.setName("Filter");
		entity.setType(Filter.TYPE.STRING);
		entity.setFamily(this.familyFilter);
		return entity;
	}

	@Override
	protected void modify(Filter entity) {
		entity.setName(entity.getName() + " updated");
		entity.setType(Filter.TYPE.INTEGER);
	}

	@Override
	protected void assertEquals(Filter one, Filter two) {
		assert one.getName().equals(two.getName());
		assert one.getType().equals(two.getType());
	}

	@Override
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void crud() {
		super.crud();
	}

	@Override
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void findAll() {
		super.findAll();
	}

	@Override
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void count() {
		super.count();
	}

}
