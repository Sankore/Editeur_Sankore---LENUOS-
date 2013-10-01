package com.paraschool.editor.catalog.web.models.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.paraschool.editor.catalog.web.models.FilterValueModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="values")
public class RestFilterValueModel extends RestElement<FilterValueModel> {

	@XmlElement(name = "value")
	public List<FilterValueModel> getList() {
		return super.getList();
	}

}
