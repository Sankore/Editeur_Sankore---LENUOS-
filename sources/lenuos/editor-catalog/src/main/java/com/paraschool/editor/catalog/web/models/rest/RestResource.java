package com.paraschool.editor.catalog.web.models.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.paraschool.editor.catalog.web.models.ResourceModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=RestElement.ROOT_CATALOG)
public class RestResource extends RestElement<ResourceModel> {

	@XmlElement(name = "resource")
	public List<ResourceModel> getList() {
		return super.getList();
	}

}
