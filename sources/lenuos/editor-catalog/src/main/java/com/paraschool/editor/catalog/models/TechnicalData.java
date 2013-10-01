package com.paraschool.editor.catalog.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.paraschool.editor.catalog.models.util.EntityId;

/**
 * Define technical data foe a resource.
 * @author blamouret
 *
 */
@Entity
@Table(name="TECHNICAL_DATA")
public class TechnicalData extends EntityId {

	private static final long serialVersionUID = -609833354642052350L;

	@Size(max = 32)
	@Column(name="TD_TYPE")
    private String type;

    @Size(max = 32)
	@Column(name="TD_VALUE")
    private String value;
    
    

    public TechnicalData() {
		super();
	}

	public TechnicalData(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	/**
     * Return the type.
     * @return the type.
     */
    public String getType() {
		return type;
	}

    /**
     * Set the type.
     * @param type the type to set.
     */
	public void setType(String type) {
		this.type = type;
	}

    /**
     * Return the value.
     * @return the value.
     */
	public String getValue() {
		return value;
	}

    /**
     * Set the value.
     * @param type the value to set.
     */
	public void setValue(String value) {
		this.value = value;
	}

}
