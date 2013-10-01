package com.paraschool.editor.catalog.web.models.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.paraschool.editor.catalog.models.Filter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=RestElement.ROOT_CATALOG)
public class RestFilter extends RestElement<Filter> {

	@XmlElement(name = "filter")
	public List<Filter> getList() {
		return super.getList();
	}

}
