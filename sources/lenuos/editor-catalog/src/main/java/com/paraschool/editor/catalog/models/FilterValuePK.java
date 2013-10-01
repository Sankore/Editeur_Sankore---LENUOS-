package com.paraschool.editor.catalog.models;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.paraschool.editor.catalog.models.util.IEntity;

/**
 * Define composed primaru key of a filter value.
 * @author blamouret
 *
 */
@Embeddable
public class FilterValuePK implements IEntity {

	private static final long serialVersionUID = 547361112832257142L;

	/**
	 * Define the resource of the value.
	 */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCE_ID", nullable = false, updatable=false, insertable=false)
    private Resource resource;

	/**
	 * Define the filter of the value.
	 */
    @ManyToOne(optional = false)
    @JoinColumn(name = "FILTER_ID", nullable = false, updatable=false, insertable=false)
    private Filter filter;

    /**
     * Default constructor.
     */
    protected FilterValuePK() {
		super();
	}

    /**
     * Class constructor for a resource and a filter
     * @param resource the resource of the primary key
     * @param filter the filter of the primary key
     */
    public FilterValuePK(Resource resource, Filter filter) {
		super();
		this.setResource(resource);
		this.setFilter(filter);
	}

    /**
     * Return the resource.
     * @return the resource.
     */
	Resource getResource() {
		return resource;
	}

	/**
	 * Set the resource.
	 * @param resource the resource to set.
	 */
	private void setResource(Resource resource) {
		this.resource = resource;
	}

    /**
     * Return the filter.
     * @return the filter.
     */
	Filter getFilter() {
		return filter;
	}

	/**
	 * Set the filter.
	 * @param filter the resource to set.
	 */
	private void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	
    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof FilterValuePK)) {
			return false;
		} else {
			return this.hashCode() == obj.hashCode();
		}
	}
    /**
     * Returns a hash code value for the object.
	 */
	public int hashCode() {
		super.hashCode();
		StringBuilder builder = new StringBuilder();
		builder.append(this.getResource().getId());
		builder.append("-");
		builder.append(this.getFilter().getId());
		return builder.toString().hashCode();
	}
}
