package com.paraschool.editor.catalog.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.web.models.FilterValueModel;
import com.paraschool.editor.catalog.web.models.ResourceModel;
import com.paraschool.editor.catalog.web.models.rest.RestFile;
import com.paraschool.editor.catalog.web.models.rest.RestFilterValueModel;
import com.paraschool.editor.catalog.web.models.rest.RestResource;

public class ResourceRestIT extends RestCatalogIT<RestResource, ResourceModel> {
	
	private static final String BASE_URL = RestCatalogIT.BASE_URL + ResourceController.requestMapping + "/";
	private List<FamilyFilter> families;
	private List<Filter> filters;
	private Date initDate = null;
	
	/**
	 * Return REST url to used.
	 */
	@Override
	protected String getUrl() {
		return ResourceRestIT.BASE_URL;
	}

	/**
	 * Assert equals element.
	 */
	protected void assertElement(ResourceModel one, ResourceModel two) {
		List<Filter> filters;
		try {
			filters = this.getFilterTester().list().getList();
			for (Filter filter : filters) {
				FilterValueModel oneValue = one.getFilterValues().get(filter.getId());
				FilterValueModel twoValue = two.getFilterValues().get(filter.getId());
				assertEquals("Not same key",oneValue.getKey(), twoValue.getKey());
				assertEquals("Not same type",oneValue.getTYPE(), twoValue.getTYPE());
				assertEquals("Not same value",oneValue.getValue(), twoValue.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	 * Return REST filter service.
	 * @return
	 */
	private FilterRestIT getFilterTester() {
		FilterRestIT tester = new FilterRestIT();
		tester.restTemplate = super.restTemplate;
		return tester;
	}
	
	/**
	 * Create family filter for each test.
	 */
	private void createFamilies() {
		try {
			int length = 10;
			FamilyFilterRestIT familyTester = this.getFamilyFilterTester();
			this.families = new ArrayList<FamilyFilter>(length);
			for (int i = 0; i < length; i++) {
				FamilyFilter entity = new FamilyFilter();
				entity.setName("Family " + i);
				entity = familyTester.create(entity);
				this.families.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove family filter for each test.
	 */
	private void removeFamilies() {
		try {
			if (this.families != null && !this.families.isEmpty()) {
				FamilyFilterRestIT familyTester = this.getFamilyFilterTester();
				for (FamilyFilter family : this.families) {
					familyTester.delete(family);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create filter for each test.
	 */
	private void createFilters() {
		try {
			int length = Filter.TYPE.values().length;
			this.filters = new ArrayList<Filter>(length);
			FilterRestIT filterTester = this.getFilterTester();
			for (int i = 0; i < length; i++) {
				Filter entity = new Filter();
				entity.setName("Filter " + i);
				entity.setType(Filter.TYPE.values()[i]);
				entity.setFamily(this.families.get(i % this.families.size()));
				entity = filterTester.create(entity);
				this.filters.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove filter for each test.
	 */
	private void removeFilters() {
		try {
			if (this.filters != null && !this.filters.isEmpty()) {
				FilterRestIT filterTester = this.getFilterTester();
				for (Filter filter : this.filters) {
					filterTester.delete(filter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * JUnit test before executed one test.
	 */
	@Before
	public void before() {
		this.initDate = new Date();
		this.createFamilies();
		this.createFilters();
	}
	
	/**
	 * JUnit test after executed one test.
	 */
	@After
	public void after() {
		this.removeFilters();
		this.removeFamilies();
		this.initDate = null;
	}
	
	/**
	 * Return a new not saved resource.
	 * @return
	 */
	private ResourceModel init() {
		ResourceModel resource = new ResourceModel();
		RestFilterValueModel values = new RestFilterValueModel();
		List<Filter> filters = null;
		try {
			filters = this.getFilterTester().list().getList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Filter filter : filters) {
			Object value = null;
			switch (filter.getType()) {
			case BOOLEAN:
				value = Boolean.TRUE;
				break;
			case DATE:
				value = this.initDate;
				break;
			case INTEGER:
				value = Integer.valueOf(filter.getId());
				break;
			case STRING:
				value = new String("Value of " + filter.getName());
				break;
			}
			FilterValueModel one = new FilterValueModel(value, filter);
			values.getList().add(one);
		}
		resource.setValues(values);
		return resource;
	}
	
	/**
	 * JUnit CRUD test.
	 * @throws Exception
	 */
	@Test
	public void crud() throws Exception{
		// Init
		ResourceModel created = null;
		ResourceModel readed = null;
		ResourceModel updated = null;

		// Create
		created = this.create();
		// Read
		readed = this.read(created);
		// List
		super.list(readed.getId());
		// update
		updated = this.update(readed);
		// search
		this.search("Value");
		// delete
		this.delete(updated);
	}
	
	/**
	 * Create a new Resource with REST service.
	 * @return
	 * @throws Exception
	 */
	public ResourceModel create() throws Exception{
		ResourceModel resource = this.init();
		return super.create(resource);
	}
	
	/**
	 * Update a resource with REST service.
	 */
	protected ResourceModel update(ResourceModel resource) throws Exception{
		RestFile restFile = new RestFile();
		restFile.setContentType("text/plain");
		restFile.setFilename("file.txt");
		restFile.setBinary("Content of the file".getBytes());
		resource.setBinaryFile(restFile);
		return super.update(resource);
	}
	
	/**
	 * Read a resource with REST service.
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public ResourceModel read(ResourceModel resource) throws Exception{
		return super.read(resource, resource.getId());
	}

	/**
	 * Delete a resource with REST service
	 * @param resource
	 * @throws Exception
	 */
	public void delete(ResourceModel resource) throws Exception{
		super.delete(resource.getId(), "resource.error.AssertNull.message");
	}

	/**
	 * Search resources with REST service
	 * @param text
	 * @throws Exception
	 */
	public void search(String text) throws Exception{
		HashMap<String,String> parameters = new HashMap<String,String>();
		parameters.put("text",text);
		List<ResourceModel> resources = super.doGet(parameters).getList();
		assertNotSame("Search should return more than one elements",
				resources.isEmpty(), true);
	}
	
}