package com.paraschool.editor.catalog.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import com.paraschool.editor.catalog.models.util.EntityId;

/**
 * Define a resource
 * @author blamouret
 *
 */
@Entity
@Table(name="RESOURCE")
@Indexed
public class Resource extends EntityId {

	private static final long serialVersionUID = 5417904723309087769L;

	/**
	 * Define the thumbnail url.
	 */
	@Size(max = 1024)
	@Column(name="THUMBNAIL_URL")
    private String thumbnailUrl;

	/**
	 * Define the file url.
	 */
    @Size(max = 1024)
	@Column(name="FILE_URL")
    private String fileUrl;

    /**
     * Define filter's value.
     */
    @Field
    @FieldBridge(impl = FilterValueBridge.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy="pk.resource", fetch = FetchType.LAZY)
	private Set<FilterValue> filterValues = new HashSet<FilterValue>();
    
    /**
     * Define the virtual repository of the resource.
     */
    @ManyToOne
    @JoinColumn(name="REPO_ID")
    private VirtualRepository virtualRepository;

    /**
     * Define technicals data.
     */
	@OneToMany(cascade = CascadeType.ALL)
    private Set<TechnicalData> technicalDatas = new java.util.HashSet<TechnicalData>();

	/**
	 * Return the thumbnail url.
	 * @return the thumbnail url.
	 */
    public String getThumbnailUrl() {
		return thumbnailUrl;
	}

    /**
     * Set the thumbnail url. 
     * @param thumbnailUrl the thumbnail url to set.
     */
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	/**
	 * Return the file url.
	 * @return the file url.
	 */
	public String getFileUrl() {
		return fileUrl;
	}

    /**
     * Set the file url. 
     * @param fileUrl the file url to set.
     */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * Return the filter's value.
	 * @param filter the filter to get value for this resource. 
	 * @return the filter's value.
	 */
    @Transient
    public Object getValue(Filter filter) {
    	return this.getFilterValue(filter).getValue();
    }
    
	/**
	 * Return the filterValue object.
	 * @param filter the filter to get filterValue for this resource. 
	 * @return the filterValue.
	 */
    @Transient
    private FilterValue getFilterValue(Filter filter) {
    	for (FilterValue v : this.filterValues) {
    		if (filter.getId().equals(v.getFilter().getId())) {
    			return v;
    		}
    	}
    	FilterValue v = this.createValue(filter);
    	this.filterValues.add(v);
    	return v;
    }
    
    /**
     * Set a value for a filter.
     * @param filter the filtee to set the value.
     * @param value the value to set.
     */
    public void setValue(Filter filter, Object value) {
    	FilterValue v = this.getFilterValue(filter);
    	v.setValue(value);
    }
    
    /**
     * Create a filterValue object according to a filter and this resource.
     * The filterValues is supposed not already existed.
     * @param filter the filtee to create the object.
     * @return the new filterValue object.
     */
    private FilterValue createValue(Filter filter) {
    	FilterValue v = new FilterValue(new FilterValuePK(this, filter));
    	return v;
    }

    /**
     * Return the virtual repository of the resource.
     * @return the virtual repository of the resource.
     */
    public VirtualRepository getVirtualRepository() {
		return virtualRepository;
	}

    /**
     * Set the virtual repository of the resource.
     * @param virtualRepository the virtual repository of the resource to set.
     */
	public void setVirtualRepository(VirtualRepository virtualRepository) {
		this.virtualRepository = virtualRepository;
	}

	/**
	 * Return technical datas.
	 * @return technical datas.
	 */
	public Set<TechnicalData> getTechnicalDatas() {
		return technicalDatas;
	}

	/**
	 * Set the technical datas of the resource.
	 * @param technicalDatas
	 */
	public void setTechnicalDatas(Set<TechnicalData> technicalDatas) {
		this.technicalDatas = technicalDatas;
	}

}
