package com.paraschool.editor.catalog.web.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.models.Resource;
import com.paraschool.editor.catalog.models.TechnicalData;
import com.paraschool.editor.catalog.web.models.rest.RestFilterValueModel;
import com.paraschool.editor.catalog.web.models.rest.RestFile;

public class ResourceModel implements Serializable {

	private static final long serialVersionUID = -4494710616096525637L;
	private Integer id;
	private Integer version;
	private String thumbnailUrl;
	private String fileUrl;
	@Valid 
	private Map<Integer, FilterValueModel> filterValues;
	private Set<TechnicalData> technicalDatas;
	@XmlElement
	private RestFile binaryFile;
	private CommonsMultipartFile fileData;
	
	private static final String downloadServletName = "/downloadServlet?fileUrl=";
	
	public ResourceModel() {
		super();
		this.setFilterValues(new HashMap<Integer, FilterValueModel>() {
			private static final long serialVersionUID = -4812824257401403070L;

			@Override
			public FilterValueModel get(Object key) {
				FilterValueModel value = super.get(key);
				if (value == null) {
					value = new FilterValueModel();
					super.put((Integer)key, value);
				}
				return value;
			}
		});
	}

	public ResourceModel(Resource resource, List<Filter> filters, String contextPath) {
		super();
		this.setFilterValues(new HashMap<Integer, FilterValueModel>());
		this.setId(resource.getId());
		this.setVersion(resource.getVersion());
		if (contextPath != null) {
			if (resource.getFileUrl() != null) {
				String url = contextPath + downloadServletName + resource.getFileUrl();
				this.setFileUrl(url);
			}
			if (resource.getThumbnailUrl() != null) {
				String url = contextPath + downloadServletName + resource.getThumbnailUrl();
				this.setThumbnailUrl(url);
			}
		}
		for (Filter filter : filters) {
			this.getFilterValues().put(filter.getId(),new FilterValueModel(resource,filter));
		}
		this.setTechnicalDatas(resource.getTechnicalDatas());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@XmlTransient
	public Map<Integer, FilterValueModel> getFilterValues() {
		return filterValues;
	}
	
	public RestFilterValueModel getValues() {
		RestFilterValueModel list = new RestFilterValueModel();
		list.getList().addAll(this.getFilterValues().values());
		return list;
	}

	public void setValues(RestFilterValueModel list) {
		this.getFilterValues().clear();
		for (FilterValueModel one : list.getList()) {
			this.getFilterValues().put(one.getKey(), one);
		}
	}

	public Set<Integer> getFilterKeys() {
		return filterValues.keySet();
	}

	private void setFilterValues(Map<Integer, FilterValueModel> values) {
		this.filterValues = values;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Set<TechnicalData> getTechnicalDatas() {
		return technicalDatas;
	}

	public void setTechnicalDatas(Set<TechnicalData> technicalDatas) {
		this.technicalDatas = technicalDatas;
	}

	public RestFile getBinaryFile() {
		return binaryFile;
	}

	public void setBinaryFile(RestFile binaryFile) {
		this.binaryFile = binaryFile;
	}

	@XmlTransient
	public CommonsMultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(CommonsMultipartFile fileData) {
		this.fileData = fileData;
	}

	public void toResource(Resource resource, List<Filter> filters) {
		FilterValueModel value = null;
		for (Filter filter : filters) {
			value = this.getFilterValues().get(filter.getId());
			if (value == null) {
				resource.setValue(filter, null);
			} else {
				value.toFilterValue(resource, filter);
			}
		}
	}
	
	public static List<ResourceModel> toResource(List<Resource> resources, List<Filter> filters, String contextPath) {
		List<ResourceModel> list = new ArrayList<ResourceModel>(resources.size());
		for (Resource one : resources) {
			list.add(new ResourceModel(one, filters, contextPath));
		}
		return list;
	}
	
	@XmlTransient
	public boolean isBinaryFile() {
		boolean newFile = this.getBinaryFile() != null 
			&& this.getBinaryFile().getBinary() != null
			&& this.getBinaryFile().getBinary().length > 0;
		return newFile;
	}
	
	@XmlTransient
	public boolean isUploadedFile() {
		boolean newFile = (this.getFileData() != null && !this.getFileData().isEmpty());
		return newFile;
	}
	
	@XmlTransient
	public boolean isNewFile() {
		return this.isBinaryFile() || this.isUploadedFile();
	}
	
}
