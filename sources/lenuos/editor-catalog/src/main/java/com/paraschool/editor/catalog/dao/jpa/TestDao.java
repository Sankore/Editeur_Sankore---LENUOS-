package com.paraschool.editor.catalog.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.paraschool.editor.catalog.models.Test;

@Repository("testDao")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TestDao implements com.paraschool.editor.catalog.dao.TestDao {

	@PersistenceContext protected EntityManager em;
	
	public Test find(Integer id) {
		return em.find(Test.class, id);
	}
 
	@SuppressWarnings("unchecked")
	public List<Test> findAll() {
		String query = "SELECT o FROM "+Test.class.getName()+" o";
		return (List<Test>)em.createQuery(query).getResultList();
	}
	
	@Transactional(readOnly = false)
	public void save(Test entity) {
		if(entity.getId() == null)
			em.persist(entity);
		else
			em.merge(entity); 
	}

}
