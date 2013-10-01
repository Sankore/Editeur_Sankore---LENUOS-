package com.paraschool.editor.catalog.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
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
import com.paraschool.editor.catalog.models.Resource;
import com.paraschool.editor.catalog.models.TechnicalData;
import com.paraschool.editor.catalog.models.VirtualRepository;

/**
 * Define JUnit test for a Resource entity.
 * 
 * @author blamouret
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/paraschool/editor/catalog/*.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class ResourceDaoTest extends EntityDaoTest<Resource> {

	/**
	 * Family Resource DAO
	 */
	@Autowired
	ResourceDao resourceDao;

	/**
	 * Family filter DAO
	 */
	@Autowired
	FilterDao filterDao;

	/**
	 * Family FamilyFilter DAO
	 */
	@Autowired
	FamilyFilterDao familyDao;

	/**
	 * Family VirtualRepository DAO
	 */
	@Autowired
	VirtualRepositoryDao repDao;
	
	/**
	 * Family FamilyFilter created for this test.
	 */
	List<FamilyFilter> familyFilters;
	/**
	 * Family filter created for this test.
	 */
	List<Filter> filters;
	/**
	 * Family VirtualRepository created for this test.
	 */
	List<VirtualRepository> repositories;
	/**
	 * Family VirtualRepository created for this test.
	 */
	List<Resource> resources;

	/**
	 * Create family filter for each test.
	 */
	private void createFamilies() {
		int length = 10;
		this.familyFilters = new ArrayList<FamilyFilter>(length);
		for (int i = 0; i < length; i++) {
			FamilyFilter entity = new FamilyFilter();
			entity.setName("Family " + i);
			entity = this.familyDao.save(entity);
			this.familyFilters.add(entity);
		}
	}

	/**
	 * Create Virtual repository for each test.
	 */
	private void createRepositories() {
		int length = 2;
		this.repositories = new ArrayList<VirtualRepository>(length);
		for (int i = 0; i < length; i++) {
			VirtualRepository entity = new VirtualRepository();
			entity.setName("Family " + i);
			entity = this.repDao.save(entity);
			this.repositories.add(entity);
		}
	}
	
	private void createResources() {
		this.resources = new ArrayList<Resource>();
		// Create one resource
		Resource entity = this.init();
		entity = this.getDAO().save(entity);
		this.resources.add(entity);
		// Create another resource
		entity = this.init();
		for (Filter filter : this.filters) {
			Object value = null;
			switch (filter.getType()) {
			case BOOLEAN:
				value = Boolean.TRUE;
				break;
			case DATE:
				value = new Date();
				break;
			case INTEGER:
				value = Integer.valueOf(filter.getId());
				break;
			case STRING:
				value = new String("Le Café est bon ce matin" + filter.getName());
				break;
			}
			entity.setValue(filter, value);
		}
		entity = this.getDAO().save(entity);
		this.resources.add(entity);
	}

	/**
	 * Remove Virtual repository for each test.
	 */
	private void removeRepositories() {
		for (VirtualRepository entity : this.repositories) {
			this.repDao.delete(this.repDao.find(entity.getId()));
		}
		this.repositories.clear();
		this.repositories = null;
	}

	/**
	 * Remove Virtual repository for each test.
	 */
	private void removeResources() {
		if (this.resources == null) {
			return;
		}
		Resource toRemove;
		for (Resource entity : this.resources) {
			toRemove = this.resourceDao.find(entity.getId());
			if (toRemove != null) {
				this.resourceDao.delete(toRemove);
			}
		}
		this.resources.clear();
		this.resources = null;
	}

	/**
	 * Remove family filter for each test.
	 */
	private void removeFamilies() {
		for (FamilyFilter entity : this.familyFilters) {
			this.familyDao.delete(this.familyDao.find(entity.getId()));
		}
		this.familyFilters.clear();
		this.familyFilters = null;
	}

	/**
	 * Create filter for each test.
	 */
	private void createFilters() {
		int length = Filter.TYPE.values().length;
		this.filters = new ArrayList<Filter>(length);
		for (int i = 0; i < length; i++) {
			Filter entity = new Filter();
			entity.setName("Filter " + i);
			entity.setType(Filter.TYPE.values()[i]);
			entity.setFamily(this.familyFilters.get(i % this.familyFilters.size()));
			entity = this.filterDao.save(entity);
			this.filters.add(entity);
		}
	}

	/**
	 * Remove filter for each test.
	 */
	private void removeFilters() {
		for (Filter entity : this.filters) {
			this.filterDao.delete(this.filterDao.find(entity.getId()));
		}
		this.filters.clear();
		this.filters = null;
	}

	/**
	 * JUnit test before executed one test.
	 */
	@Before
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void before() {
		assert this.getDAO() != null;
		assert this.familyDao != null;
		assert this.filterDao != null;
		assert this.repDao != null;
		this.initDate = new Date();

		this.createFamilies();
		this.createFilters();
		this.createRepositories();
		this.createResources();
	}

	/**
	 * JUnit test after executed one test.
	 */
	@After
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void after() {
		this.removeResources();
		this.removeFilters();
		this.removeFamilies();
		this.removeRepositories();
		this.initDate = null;
	}

	@Override
	protected EntityDao<Resource> getDAO() {
		return resourceDao;
	}

	private static final int TD_LENGTH = 10;
	private Boolean TD_VALUE = Boolean.TRUE;
	private Date initDate = null;

	@Override
	protected Resource init() {
		Resource entity = new Resource();
		entity.setFileUrl("file url");
		entity.setThumbnailUrl("thumbnail url");
		entity.setVirtualRepository(this.repositories.get(0));
		// Filter value
		for (Filter filter : this.filters) {
			Object value = null;
			switch (filter.getType()) {
			case BOOLEAN:
				value = Boolean.TRUE;
				break;
			case DATE:
				value = initDate;
				break;
			case INTEGER:
				value = Integer.valueOf(filter.getId());
				break;
			case STRING:
				value = new String("Value of " + filter.getName());
				break;
			}
			entity.setValue(filter, value);
		}
		// Technical Data (Boolean.TRUE as value)
		for (int i = 0; i < TD_LENGTH; i++) {
			TechnicalData data = new TechnicalData();
			data.setValue(TD_VALUE.toString());
			data.setType("Type " + i);
			entity.getTechnicalDatas().add(data);
		}
		return entity;
	}

	@Override
	protected void modify(Resource entity) {
		entity.setFileUrl(entity.getFileUrl() + " updated");
		entity.setThumbnailUrl(entity.getThumbnailUrl() + " updated");
		entity.setVirtualRepository(this.repositories.get(1));

		// Create a new Filter
		Filter f = new Filter();
		f.setName("New filter");
		f.setType(Filter.TYPE.BOOLEAN);
		f.setFamily(this.familyFilters.get(0));
		this.filterDao.save(f);
		this.filters.add(f);

		// Update filter values
		for (Filter filter : this.filters) {
			Object value = null;
			switch (filter.getType()) {
			case BOOLEAN:
				value = Boolean.FALSE;
				break;
			case DATE:
				value = new Date();
				break;
			case INTEGER:
				value = Integer.valueOf(filter.getId() + 10);
				break;
			case STRING:
				value = new String("Updated value of " + filter.getName());
				break;
			}
			entity.setValue(filter, value);
		}
		// Update technical data
		if (entity.getTechnicalDatas().size() == TD_LENGTH) {
			TD_VALUE = Boolean.FALSE;
			// Remove 2 technical data
			TechnicalData data = entity.getTechnicalDatas().iterator().next();
			entity.getTechnicalDatas().remove(data);
			data = entity.getTechnicalDatas().iterator().next();
			entity.getTechnicalDatas().remove(data);
			// add one technical data (Boolean.FALSE as value)
			data = new TechnicalData();
			data.setValue(TD_VALUE.toString());
			data.setType("updated type ");
			entity.getTechnicalDatas().add(data);
			Iterator<TechnicalData> iter = entity.getTechnicalDatas()
					.iterator();
			// Set value for all technical data (Boolean.FALSE as value)
			while (iter.hasNext()) {
				iter.next().setValue(TD_VALUE.toString());
			}
		}
	}

	@Override
	protected void assertEquals(Resource one, Resource two) {
		assert one.getFileUrl().equals(two.getFileUrl());
		assert one.getThumbnailUrl().equals(two.getThumbnailUrl());
		for (Filter filter : this.filters) {
			assert one.getValue(filter) != null;
			assert two.getValue(filter) != null;
			assert one.getValue(filter).equals(two.getValue(filter));
		}
		assert one.getTechnicalDatas().size() == two.getTechnicalDatas().size();
		assert one.getVirtualRepository().getId().equals(two.getVirtualRepository()
				.getId());
	}

	@Override
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void crud() {
		super.crud();
	}

	@Override
	protected void assertEntity(Resource entity) {
		assert Hibernate.isInitialized(entity.getTechnicalDatas());
		for (Filter filter : this.filters) {
			assert entity.getValue(filter) != null;
		}
		assert !entity.getTechnicalDatas().isEmpty();
		Boolean value = null;
		Iterator<TechnicalData> iter = entity.getTechnicalDatas().iterator();
		while (iter.hasNext()) {
			value = Boolean.valueOf(iter.next().getValue());
			assert value == TD_VALUE;
		}
		int length = entity.getTechnicalDatas().size();
		assert length == TD_LENGTH || length == TD_LENGTH - 1;
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

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void search() {
		// Search one resource
		List<Resource> list = this.resourceDao.search("Café", this.filters);
		assert list.size() == 0;
	}

}
