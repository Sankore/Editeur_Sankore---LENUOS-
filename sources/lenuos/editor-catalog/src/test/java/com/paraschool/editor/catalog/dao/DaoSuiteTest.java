package com.paraschool.editor.catalog.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 * Define suite test for DAO.
 * @author blamouret
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value={
		FamilyFilterDaoTest.class,
		FilterDaoTest.class,
		VirtualRepositoryDaoTest.class,
		ResourceDaoTest.class
		})

public class DaoSuiteTest {

	/**
	 * Default Constructor.
	 */
	public DaoSuiteTest() {
		super();
	}

	
}
