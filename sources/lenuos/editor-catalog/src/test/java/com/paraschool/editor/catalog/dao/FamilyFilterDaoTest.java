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

/**
 * Define JUnit test for a FamilyFilter entity.
 * @author blamouret
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/paraschool/editor/catalog/*.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class FamilyFilterDaoTest extends EntityDaoTest<FamilyFilter> {

	/**
	 * Family filter DAO
	 */
	@Autowired
	FamilyFilterDao familyDao;

	/**
	 * JUnit test before executed one test.
	 */
	@Before
	public void before() {
		assert this.getDAO() != null;
	}

	/**
	 * JUnit test after executed one test.
	 */
	@After
	public void after() {
	}

	@Override
	protected EntityDao<FamilyFilter> getDAO() {
		return familyDao;
	}

	@Override
	protected FamilyFilter init() {
		FamilyFilter entity = new FamilyFilter();
		entity.setName("Family");
		return entity;
	}

	@Override
	protected void modify(FamilyFilter entity) {
		entity.setName(entity.getName() + " updated");
	}

	@Override
	protected void assertEquals(FamilyFilter one, FamilyFilter two) {
		assert one.getName().equals(two.getName());
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
