package com.paraschool.editor.catalog.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.web.models.rest.RestFilter;

public class FilterRestIT extends RestCatalogIT<RestFilter, Filter> {
	
	private static final String BASE_URL = RestCatalogIT.BASE_URL + FilterController.requestMapping + "/";
	private FamilyFilter family;
	
	/**
	 * Return REST url to used.
	 */
	@Override
	protected String getUrl() {
		return FilterRestIT.BASE_URL;
	}

	/**
	 * Assert equals element.
	 */
	protected void assertElement(Filter one, Filter two) {
		assertEquals("Not same name", one.getName(), two.getName());
		if (one.getId() != null && two.getId() != null) {
			assertEquals("Not same id", one.getId().intValue(), two.getId().intValue());
		} else {
			assertNotNull("Null id" , two.getId());
		}
		assertEquals("Not same family", one.getFamily().getId(), two.getFamily().getId());
		assertEquals("Not same type", one.getType(), two.getType());
	}
	
	/**
	 * Return REST family filter service.
	 * @return
	 */
	private FamilyFilterRestIT getFamilyFilterTester() {
		FamilyFilterRestIT tester = new FamilyFilterRestIT();
		tester.restTemplate = super.restTemplate;
		return tester;
	}
	
	/**
	 * JUnit test before executed one test.
	 */
	@Before
	public void before() {
		try {
			this.family = this.getFamilyFilterTester().create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * JUnit test after executed one test.
	 */
	@After
	public void after() {
		try {
			if (this.family != null) {
				this.getFamilyFilterTester().delete(this.family);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return a new not saved filter.
	 * @return
	 */
	private Filter init() {
		Filter filter = new Filter();
		filter.setName("New Rest Filter");
		filter.setType(Filter.TYPE.BOOLEAN);
		filter.setFamily(this.family);
		return filter;
	}
	
	/**
	 * JUnit CRUD test.
	 * @throws Exception
	 */
	@Test
	public void crud() throws Exception{
		// Init
		Filter created = null;
		Filter readed = null;
		Filter updated = null;

		// Create
		created = this.create();
		// Read
		readed = this.read(created);
		// List
		super.list(readed.getId());
		// update
		updated = super.update(readed);
		// delete
		this.delete(updated);
	}
	
	/**
	 * Create a new filter with REST service.
	 * @return
	 * @throws Exception
	 */
	public Filter create() throws Exception{
		Filter filter = this.init();
		return super.create(filter);
	}
	
	/**
	 * Read a filter with REST service.
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Filter read(Filter filter) throws Exception{
		return super.read(filter, filter.getId());
	}

	/**
	 * Delete a filter with REST service
	 * @param filter
	 * @throws Exception
	 */
	public void delete(Filter filter) throws Exception{
		super.delete(filter.getId(), "filter.error.AssertNull.message");
	}

}