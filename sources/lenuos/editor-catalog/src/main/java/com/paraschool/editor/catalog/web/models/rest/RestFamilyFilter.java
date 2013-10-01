package com.paraschool.editor.catalog.web.models.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.paraschool.editor.catalog.models.FamilyFilter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=RestElement.ROOT_CATALOG)
public class RestFamilyFilter extends RestElement<FamilyFilter> {

	@XmlElement(name = "familyFilter")
	public List<FamilyFilter> getList() {
		return super.getList();
	}

}
