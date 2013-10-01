package com.paraschool.editor.catalog.dao;

import java.util.List;

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
import com.paraschool.editor.catalog.models.VirtualRepository;

/**
 * Define JUnit test for a VirtualRepository entity.
 * @author blamouret
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/paraschool/editor/catalog/*.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class VirtualRepositoryDaoTest extends EntityDaoTest<VirtualRepository> {

	/**
	 * VirtualRepository DAO
	 */
	@Autowired
	VirtualRepositoryDao repDao;
	
	/**
	 * JUnit test before executed one test.
	 */
	@Before
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void before() {
		assert this.getDAO() != null;
	}

	/**
	 * JUnit test after executed one test.
	 */
	@After
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void after() {
	}

	@Override
	protected EntityDao<VirtualRepository> getDAO() {
		return repDao;
	}
	
	@Override
	protected VirtualRepository init() {
		VirtualRepository entity = new VirtualRepository();
		entity.setName("VRepo");
		for (int i=0;i<10;i++) {
			VirtualRepository sub =  new VirtualRepository();
			sub.setName("Sub VRepo " + i);
			sub.setParent(entity);
			for (int j=0;j<10;j++) {
				VirtualRepository subsub =  new VirtualRepository();
				subsub.setName("Sub Sub VRepo " + j);
				subsub.setParent(sub);
			}
		}
		return entity;
	}

	@Override
	protected void modify(VirtualRepository entity) {
		entity.setName("VRepo updated");
	}

	@Override
	protected void assertEquals(VirtualRepository one, VirtualRepository two) {
		assert one.getName().equals(two.getName());
		if (one.getParent() == null && two.getParent() != null) {
			assert one.getParent().getId().equals(two.getParent().getId());
		}
		assert one.getSubRepositories().length == two.getSubRepositories().length;
	}

	@Override
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void crud() {
		super.crud();
	}
	
	@Override
	protected void assertEntity(VirtualRepository entity) {
	}

	@Override
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void findAll() {
		List<VirtualRepository> list = this.getDAO().findAll();
		int size = list.size();
		VirtualRepository entity = super.create();
		entity = super.find(entity.getId(), true);
		size += this.size(entity);
		list = this.getDAO().findAll();
		assert size == list.size();
		this.delete(entity);
	}
	
	private int size(VirtualRepository entity) {
		if (entity == null) {
			return 0;
		}
		int size = 1;
		for (VirtualRepository sub : entity.getSubRepositories()) {
			size += this.size(sub);
		}
		return size;
	}

	@Override
	@Test
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void count() {
		super.count();
	}
	
	/**
	 * JUnit test for Readed CRUD method for existed on unexisted entity.
	 * Load sub repositories too (level 1).
	 * 
	 */
	protected VirtualRepository find(Integer id, boolean existed) {
		VirtualRepository saved = super.find(id, existed);
		if (saved != null) {
			saved.getSubRepositories();
		}
		return saved;
	}

}
