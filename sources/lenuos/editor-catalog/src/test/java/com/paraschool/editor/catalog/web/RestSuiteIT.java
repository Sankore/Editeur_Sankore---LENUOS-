package com.paraschool.editor.catalog.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 * Define suite test for REST.
 * @author blamouret
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value={
		FamilyFilterRestIT.class,
		FilterRestIT.class,
		ResourceRestIT.class,
		})

public class RestSuiteIT {

	/**
	 * Default Constructor.
	 */
	public RestSuiteIT() {
		super();
	}

	
}
