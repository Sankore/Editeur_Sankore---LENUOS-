package com.paraschool.editor.catalog.web.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.models.Resource;

public class FilterValueModel {
	
	private Integer key;
	private String label;
	@NotNull
	private Object value;
	private String type;
	
	public static final SimpleDateFormat formatDate = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");

	
	public FilterValueModel() {
		super();
	}
	
	public FilterValueModel(Resource resource, Filter filter) {
		this();
		this.setLabel(filter.getName());
		this.setValue(resource.getValue(filter));
		this.setKey(filter.getId());
		this.setType(filter.getType());
	}
	
	public FilterValueModel(Object value, Filter filter) {
		this();
		this.setLabel(filter.getName());
		this.setValue(value);
		this.setKey(filter.getId());
		this.setType(filter.getType());
	}
	

	public boolean isBoolean() {
		return this.getType() != null && Filter.TYPE.valueOf(this.getType()).isBoolean();
	}

	public boolean isDate() {
		return this.getType() != null && Filter.TYPE.valueOf(this.getType()).isDate();
	}

	public boolean isInteger() {
		return this.getType() != null && Filter.TYPE.valueOf(this.getType()).isInteger();
	}

	public boolean isString() {
		return this.getType() != null && Filter.TYPE.valueOf(this.getType()).isString();
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getValue() {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}
	
	public boolean isNull() {
		return this.value == null;
	}

	public void setValue(Object value) {
		if (value == null || value.equals("")) {
			this.value = null;
		} else if (value instanceof Date) {
			this.value = formatDate.format(value);
		} else {
			this.value = value;
		}
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public Filter.TYPE getTYPE() {
		return Filter.TYPE.valueOf(type);
	}

	private void setType(Filter.TYPE type) {
		this.type = type.toString();
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getLabel());
		builder.append("(");
		builder.append(this.getKey());
		builder.append(")=");
		builder.append(this.getValue());
		return builder.toString();
	}

	public void toFilterValue(Resource resource, Filter filter) {
		if (filter.getType().isDate()) {
			try {
				resource.setValue(filter, formatDate.parse((String)this.getValue()));
			} catch (ParseException e) {
				resource.setValue(filter, null);
			}
		} else {
			try {
				resource.setValue(filter, this.getValue());
			} catch (Exception e) {
				resource.setValue(filter, null);
			}
		}
	}
	
}
