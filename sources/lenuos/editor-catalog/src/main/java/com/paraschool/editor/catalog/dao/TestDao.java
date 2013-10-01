package com.paraschool.editor.catalog.dao;

import java.util.List;

import com.paraschool.editor.catalog.models.Test;

public interface TestDao {

	public Test find(Integer id); 
	public List<Test> findAll();
	public void save(Test entity);

}
