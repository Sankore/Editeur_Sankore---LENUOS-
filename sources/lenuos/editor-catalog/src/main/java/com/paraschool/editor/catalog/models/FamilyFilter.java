package com.paraschool.editor.catalog.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.paraschool.editor.catalog.models.util.EntityId;

/**
 * Define a family filter.
 * @author blamouret
 *
 */
@Entity
@Table(name="FAMILY_FILTER")
public class FamilyFilter extends EntityId {

	private static final long serialVersionUID = 1063515319711682705L;
	
	/**
	 * Define the name.
	 */
	@NotNull
    @Size(min= 1 , max = 255)
    @Column(name="NAME")
    private String name;

	/**
	 * Return the name.
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * @param name the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
