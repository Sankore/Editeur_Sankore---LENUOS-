package com.paraschool.editor.catalog.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.paraschool.editor.catalog.models.util.EntityId;

/**
 * Define a resource filter.
 * @author blamouret
 *
 */
@Entity
@Table(name="FILTER")
public class Filter extends EntityId {
	
	private static final long serialVersionUID = 5974893622607854096L;

	/**
	 * Define filter type.
	 * 
	 */
	public enum TYPE {
		BOOLEAN,
		INTEGER,
		STRING,
		DATE;
		
		public boolean isBoolean() {
			return this.equals(BOOLEAN);
		}
		public boolean isInteger() {
			return this.equals(INTEGER);
		}
		public boolean isString() {
			return this.equals(STRING);
		}
		public boolean isDate() {
			return this.equals(DATE);
		}
	}

	/**
	 * Define the name of the filter
	 */
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name="NAME")
    private String name;

    /**
     * Define the type.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name="FTYPE")
    private TYPE type;

    /**
     * Define the filter's family
     */
    @ManyToOne
    @NotNull
    @JoinColumn(name="FAMILY_ID")
    private FamilyFilter family;

    /**
     * Return the name of the filter.
     * @return the name of the filter.
     */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the filter
	 * @param name the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the type of the filter.
	 * @return the type of the filter.
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * Set the type of the filter.
	 * @param type the type of the filter to set.
	 */
	public void setType(TYPE type) {
		this.type = type;
	}

	/**
	 * Return the filter's family.
	 * @return the filter's family.
	 */
	public FamilyFilter getFamily() {
		return family;
	}

	/**
	 * Set the filter's family.
	 * @param family the filter's family to set.
	 */
	public void setFamily(FamilyFilter family) {
		this.family = family;
	}
    
}
