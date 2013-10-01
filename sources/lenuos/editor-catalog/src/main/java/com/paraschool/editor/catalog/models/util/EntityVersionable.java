package com.paraschool.editor.catalog.models.util;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Define persistence class that can be versionable.
 * @author blamouret
 *
 */
@MappedSuperclass
public abstract class EntityVersionable implements IEntity {
    
	private static final long serialVersionUID = 3832879403464026860L;
	
	/**
	 * Define the version number of the object.
	 */
	@Version
    @Column(name = "version")
    private Integer version;

	/**
	 * Return the version number of the object.
	 * @return the version number of the object.
	 */
	public Integer getVersion() {
		return version;
	}
	
	/**
	 * Set the version number of the object.
	 * @return the version number of the object to set.
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
