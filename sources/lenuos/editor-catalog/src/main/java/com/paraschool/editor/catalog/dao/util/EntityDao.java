package com.paraschool.editor.catalog.dao.util;

import java.util.List;

import com.paraschool.editor.catalog.models.util.IEntity;

/**
 * Define dao interface.
 * @author blamouret
 *
 */
public interface EntityDao<E extends IEntity> {

	public E find(Integer id); 
	public List<E> findAll();
	public E save(E entity);
	public void delete(E entity);
	public Long count();

}
