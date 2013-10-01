package com.paraschool.editor.catalog.models;

import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.paraschool.editor.catalog.models.util.IEntity;

/**
 * Define a value for a filter on a resource.
 * @author blamouret
 *
 */
@Entity
@Table(name="FILTER_VALUE")
@AssociationOverrides( {
	@AssociationOverride(name = "pk.resource", joinColumns = @JoinColumn(name = "RESOURCE_ID")),
	@AssociationOverride(name = "pk.filter", joinColumns = @JoinColumn(name = "FILTER_ID")) })
public class FilterValue implements IEntity {

	private static final long serialVersionUID = 547361112832257142L;

	/**
	 * The integer value of the filter.
	 */
	@Column(name="INT_VALUE")
	private Integer integer;

	/**
	 * The string value of the filter.
	 */
	@Column(name="STRING_VALUE")
	@Size(max = 1024)
    private String string;

	/**
	 * The boolean value of the filter.
	 */
	@Column(name="BOOL_VALUE")
    private Boolean bool;

	/**
	 * The date value of the filter.
	 */
	@Column(name="DATE_VALUE")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date date;
    
	/**
	 * Define the primary composed key.
	 */
    @EmbeddedId
    FilterValuePK pk;

    /**
     * Default constructor.
     */
    protected FilterValue() {
		super();
	}

    /**
     * Class constructor according to a primary key
     * @param pk the primary key of the object.
     */
    public FilterValue(FilterValuePK pk) {
		super();
		this.pk = pk;
	}

    /**
     * Return the integer value.
     * @return the integer value.
     */
	public Integer getInteger() {
		return integer;
	}

	/**
	 * Set the integer value.
	 * @param value the integer value to set.
	 */
	public void setInteger(Object value) {
		if (value instanceof String) {
			this.integer = Integer.valueOf((String)value);
		} else {
			this.integer = (Integer) value;
		}
	}

    /**
     * Return the string value.
     * @return the string value.
     */
	public String getString() {
		return string;
	}

	/**
	 * Set the string value.
	 * @param value the string value to set.
	 */
	public void setString(Object value) {
		if (value != null) {
			this.string = value.toString();
		}
	}

    /**
     * Return the boolean value.
     * @return the integer value.
     */
	public Boolean getBoolean() {
		return bool;
	}

	/**
	 * Set the boolean value.
	 * @param value the boolean value to set.
	 */
	public void setBoolean(Object value) {
		if (value instanceof String) {
			this.bool = Boolean.valueOf((String)value);
		} else {
			this.bool = (Boolean) value;
		}
	}

    /**
     * Return the date value.
     * @return the date value.
     */
	public Date getDate() {
		return date;
	}

	/**
	 * Set the date value.
	 * @param value the date value to set.
	 */
	public void setDate(Object value) {
		this.date = (Date) value;
	}

    /**
     * Return the associated resource of the value.
     * @return the associated resource of the value.
     */
	Resource getResource() {
		return this.pk.getResource();
	}

    /**
     * Return the associated filter of the value.
     * @return the associated filter of the value.
     */
	Filter getFilter() {
		return this.pk.getFilter();
	}

	/**
	 * Return the value, according to the filter's type.
	 * @return the value, according to the filter's type.
	 */
	@Transient
	public Object getValue() {
		switch (this.getFilter().getType()) {
		case BOOLEAN: return this.getBoolean();
		case DATE: return this.getDate();
		case INTEGER: return this.getInteger();
		case STRING: return this.getString();
		default: return "FilterValue.get.Value() Not implemented";
		}
	}
    
	/**
	 * Set the value, according to the filter's type.
	 * The object should be compliant with the filter type.
	 * @param value the value to set.
	 */
	public void setValue(Object value) {
		switch (this.getFilter().getType()) {
		case BOOLEAN: this.setBoolean(value); break;
		case DATE: this.setDate(value); break;
		case INTEGER: this.setInteger(value); break;
		case STRING: this.setString(value); break;
		}
	}
    
	/**
	 * Set a boolean value.
	 * @param v the value to set.
	 */
	public void setValue(Boolean v) {
		this.setBoolean(v);
	}
	/**
	 * Set a integer value.
	 * @param v the value to set.
	 */
	public void setValue(Integer v) {
		this.setInteger(v);
	}
	/**
	 * Set a string value.
	 * @param v the value to set.
	 */
	public void setValue(String v) {
		this.setString(v);
	}
	/**
	 * Set a date value.
	 * @param v the value to set.
	 */
	public void setValue(Date v) {
		this.setDate(v);
	}
    
	/**
     * Returns a string representation of the object. 
	 * 
	 */
	public String toString() {
		super.toString();
		StringBuilder builder = new StringBuilder();
		builder.append(this.getFilter().getName());
		builder.append("(");
		builder.append(this.getFilter().getId());
		builder.append(") = ");
		builder.append(this.getValue());
		return builder.toString();
	}
}
