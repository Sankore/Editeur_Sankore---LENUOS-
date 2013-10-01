package com.paraschool.editor.catalog.models.util;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.search.annotations.DocumentId;

/**
 * Define persistence class with Integer id as primary key.
 * @author blamouret
 *
 */
@MappedSuperclass
public abstract class EntityId extends EntityVersionable {
	
	private static final long serialVersionUID = -9065544483451279404L;
	
    /**
     * The primary key of the entity.
     */
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @DocumentId
    private Integer id;
    
	/**
	 * Return the primary key of the entity.
	 * @return the primary key of the entity.
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Set the primary key of the entity.
	 * @param id the primary key to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
}
