package com.paraschool.editor.catalog.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.web.models.rest.RestFamilyFilter;

public class FamilyFilterRestIT extends RestCatalogIT<RestFamilyFilter, FamilyFilter> {
	
	private static final String BASE_URL = RestCatalogIT.BASE_URL + FamilyFilterController.requestMapping + "/";

	/**
	 * Return REST url to used.
	 */
	@Override
	protected String getUrl() {
		return FamilyFilterRestIT.BASE_URL;
	}

	/**
	 * Assert equals element.
	 */
	protected void assertElement(FamilyFilter one, FamilyFilter two) {
		assertEquals("Not same name", one.getName(), two.getName());
		if (one.getId() != null && two.getId() != null) {
			assertEquals("Not same id", one.getId().intValue(), two.getId().intValue());
		} else {
			assertNotNull("Null id" , two.getId());
		}
	}
	
	/**
	 * Return a new not saved filter.
	 * @return
	 */
	private FamilyFilter init() {
		FamilyFilter family = new FamilyFilter();
		family.setName("New Rest Family");
		return family;
	}
	
	/**
	 * JUnit CRUD test.
	 * @throws Exception
	 */
	@Test
	public void crud() throws Exception{
		FamilyFilter created = null;
		FamilyFilter readed = null;
		FamilyFilter updated = null;

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
	 * Create a new family with REST service.
	 * @return
	 * @throws Exception
	 */
	public FamilyFilter create() throws Exception{
		FamilyFilter family = this.init();
		return super.create(family);
	}
	
	/**
	 * Read a family with REST service.
	 * @param family
	 * @return
	 * @throws Exception
	 */
	public FamilyFilter read(FamilyFilter family) throws Exception{
		return super.read(family, family.getId());
	}

	/**
	 * Delete a family with REST service
	 * @param family
	 * @throws Exception
	 */
	public void delete(FamilyFilter family) throws Exception{
		super.delete(family.getId(), "familyFilter.error.AssertNull.message");
	}

}