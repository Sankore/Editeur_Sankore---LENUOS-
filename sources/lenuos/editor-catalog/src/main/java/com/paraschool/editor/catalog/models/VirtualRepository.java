package com.paraschool.editor.catalog.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.paraschool.editor.catalog.models.util.EntityId;

/**
 * Define a virtual repository.
 * @author blamouret
 *
 */
@Entity
@Table(name="VREPOSITORY")
public class VirtualRepository extends EntityId {

	private static final long serialVersionUID = -5804180265579924900L;

	/**
	 * Define the name of the repository.
	 */
	@NotNull
    @Size(max = 255)
    @Column(name="NAME")
    private String name;

	/**
	 * Define sub repositories.
	 */
    @OneToMany(cascade = CascadeType.ALL, mappedBy="parent")
    private Set<VirtualRepository> subRepositories = new HashSet<VirtualRepository>();

    /**
     * Define parent repository.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="PARENT_ID")
    private VirtualRepository parent;

    /**
     * Return the parent of the repository.
     * @return the parent of the repository.
     */
	public VirtualRepository getParent() {
		return parent;
	}

	/**
	 * Set the parent of the repository.
	 * @param parent  the parent of the repository to set.
	 */
	public void setParent(VirtualRepository parent) {
		if (parent.subRepositories.contains(this)) {
			parent.subRepositories.remove(this);
		}
		this.parent = parent;
		parent.subRepositories.add(this);
	}

	/**
	 * Return the name of the repository.
	 * @return the name of the repository.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the repository.
	 * @param name the name of the repository to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return sub repositories.
	 * @return sub repositories.
	 */
	public VirtualRepository[] getSubRepositories() {
		return subRepositories.toArray(new VirtualRepository[subRepositories.size()]);
	}
    
}
