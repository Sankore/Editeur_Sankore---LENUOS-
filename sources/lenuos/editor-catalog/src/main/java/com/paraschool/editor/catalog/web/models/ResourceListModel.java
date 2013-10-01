package com.paraschool.editor.catalog.web.models;

import java.util.ArrayList;
import java.util.List;

import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.models.Resource;

public class ResourceListModel {

	private Integer id;
	private String thumbnail;
	private String fileUrl;
	private List<FValue> values;
	
	public class FValue {
		private Object value;
		private Filter.TYPE type;
		
		public FValue(Resource resource, Filter filter) {
			super();
			this.value = resource.getValue(filter);
			this.type = filter.getType();
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public Filter.TYPE getType() {
			return type;
		}
		public void setType(Filter.TYPE type) {
			this.type = type;
		}
	}
	
	public ResourceListModel() {
		super();
		this.values = new ArrayList<FValue>();
	}

	public ResourceListModel(Resource resource, List<Filter> filters) {
		this();
		this.setId(resource.getId());
		this.setThumbnail(resource.getThumbnailUrl());
		this.setFileUrl(resource.getFileUrl());
		for (Filter filter : filters) {
			this.values.add(new FValue(resource,filter));
		}
	}
	
	public List<FValue> getValues() {
		return this.values;
	}

	public Integer getId() {
		return id;
	}

	private void setId(Integer id) {
		this.id = id;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	private void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	private void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public static List<ResourceListModel> toModel(List<Resource> resources, List<Filter> filters) {
		List<ResourceListModel> models = new ArrayList<ResourceListModel>(resources.size());
		for (Resource resource : resources) {
			models.add(new ResourceListModel(resource, filters));
		}
		return models;
	}

}
