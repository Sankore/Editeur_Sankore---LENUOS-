package com.paraschool.editor.catalog.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.paraschool.editor.catalog.dao.FilterDao;
import com.paraschool.editor.catalog.dao.ResourceDao;
import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.models.Resource;
import com.paraschool.editor.catalog.resource.util.AudioCreationHandler;
import com.paraschool.editor.catalog.resource.util.ImageCreationHandler;
import com.paraschool.editor.catalog.resource.util.ResourceCreationHandler;
import com.paraschool.editor.catalog.resource.util.ResourceUtil;
import com.paraschool.editor.catalog.resource.util.VideoCreationHandler;
import com.paraschool.editor.catalog.web.models.FilterValueModel;
import com.paraschool.editor.catalog.web.models.ResourceListModel;
import com.paraschool.editor.catalog.web.models.ResourceModel;
import com.paraschool.editor.catalog.web.models.SearchModel;
import com.paraschool.editor.catalog.web.models.rest.RestError;
import com.paraschool.editor.catalog.web.models.rest.RestFile;
import com.paraschool.editor.catalog.web.models.rest.RestResource;

@Controller
@RequestMapping(ResourceController.requestMapping)
public class ResourceController {

	static final Log logger = LogFactory.getLog(ResourceController.class);
	
	public static final String requestMapping = "/resources";

	private static final String VIEW_LIST = "resource/resourceList";
	private static final String VIEW_EDIT = "resource/resourceDetail";
	private static final String REDIRECT = "redirect:";
	
	@javax.annotation.Resource
	private ResourceDao resourceDao;
	
	@javax.annotation.Resource
	private FilterDao filterDao;
	
	//@javax.annotation.Resource
	//private FamilyFilterDao familyDao;
	
	@Value("${data_path}")
	private String rootname;
	
	private static final Validator validator = new Validator() {
		public boolean supports(Class<?> cls) {
			return FilterValueModel.class.isAssignableFrom(cls)
				|| ResourceModel.class.isAssignableFrom(cls)
				|| RestResource.class.isAssignableFrom(cls)
				|| SearchModel.class.isAssignableFrom(cls)
				|| RestError.class.isAssignableFrom(cls);
		}

		public void validate(Object model, Errors errors) {
			if (model instanceof ResourceModel) {
				this.validate((ResourceModel) model, errors);
			} else if (model instanceof FilterValueModel) {
				this.validate((FilterValueModel) model, errors);
			}
		};

		public void validate(ResourceModel model, Errors errors) {
			for (FilterValueModel fvalue : model.getFilterValues().values()) {
				errors.pushNestedPath("filterValues["+fvalue.getKey()+"]");
				ValidationUtils.invokeValidator(validator, fvalue, errors);
				errors.popNestedPath();
			}
		};
		
		public void validate(FilterValueModel model, Errors errors) {
			ValidationUtils.rejectIfEmpty(errors, "value", "error.validation.constraints.Default.message");
			if (!model.isNull()) {
				Filter filter = new Filter();
				filter.setType(model.getTYPE());
				Resource resource = new Resource();
				try {
					if (model.getTYPE().isDate()) {
						resource.setValue(filter,FilterValueModel.formatDate.parse((String)model.getValue()));
					} else {
						resource.setValue(filter, model.getValue());
					}
				} catch (Exception e) {
					if (filter.getType().isDate()) {
						errors.rejectValue("value", "error.validation.constraints.Date.message");
					} else if (filter.getType().isInteger()) {
						errors.rejectValue("value", "error.validation.constraints.Integer.message");
					}
				}
			}
		}};

	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }
	
	/**
	 * Initialize model's view
	 * @param filter
	 * @param model
	 */
	private void initModel(Resource resource, ModelMap model, HttpServletRequest request) {
		List<Filter> filters = filterDao.findAll();
		ResourceModel rm = new ResourceModel(resource, filters, ResourceUtil.getContextPath(request)); 
		this.initModel(rm, model);
	}

	/**
	 * Initialize model's view
	 * @param filter
	 * @param model
	 */
	private void initModel(ResourceModel resource, ModelMap model) {
		RestResource restList = new RestResource();
		restList.getList().add(resource);
		model.addAttribute("aaa", restList);
		model.addAttribute("resource", resource);
	}

	/**
	 * List resources.
	 * Browser/RESTful compliant.
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String list(@ModelAttribute("search")  SearchModel searchCriteria, ModelMap model, HttpServletRequest request) {
		logger.info("Get Resources list");
		List<Filter> filters = this.filterDao.findAll();
		List<Resource> resources;
		if (searchCriteria == null || searchCriteria.getText() == null || searchCriteria.getText().trim().isEmpty()) {
			resources = this.resourceDao.findAll();	
		} else {
			resources = this.resourceDao.search(searchCriteria.getText(), filters);
		}
		RestResource restList = new RestResource();
		restList.getList().addAll(ResourceModel.toResource(resources, filters, ResourceUtil.getContextPath(request)));
		model.addAttribute("rest", restList);
		model.addAttribute("resources", ResourceListModel.toModel(resources, filters));
		model.addAttribute("filters", filters);
		model.addAttribute("search", searchCriteria==null?new SearchModel():searchCriteria);
		return VIEW_LIST;
	}
	
	/**
	 * Build error data.
	 * @param msgKey
	 * @param model
	 * @return
	 */
	private RestResource error(String msgKey, ModelMap model) {
		RestResource restList = new RestResource();
		restList.setError(new RestError(msgKey));
		if (model != null) {
			model.addAttribute("aaa", restList);
			model.addAttribute("error", restList.getError());
		}
		return restList;
	}
	/**
	 * Get a resource, according to an id.
	 * Browser/RESTful compliant.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String read(@PathVariable("id") Integer id, ModelMap model, HttpServletRequest request) {
		logger.info("Get Resource " + id);
		Resource resource = this.resourceDao.find(id);
		if (resource == null) {
			this.error("resource.error.AssertNull.message", model);
			List<Filter> filters = filterDao.findAll();
			List<Resource> resources = this.resourceDao.findAll();
			model.addAttribute("resources", ResourceListModel.toModel(resources, filters));
			model.addAttribute("filters", filters);
			model.addAttribute("search", new SearchModel());
			return VIEW_LIST;
		} else {
			this.initModel(resource, model, request);
	        return VIEW_EDIT;
		}
	
	}
	
	/**
	 * Create a new Resource.
	 * Browser compliant.
	 * @param model
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute("resource") ResourceModel resModel, Errors result, ModelMap model, HttpServletRequest request) {
		logger.info("Create new Resource");
		if (result.hasErrors()){
			result.reject("error.validation.constraints.Default.message");
			this.initModel(resModel, model);
			return VIEW_EDIT;
		} else {
			this.save(resModel, result, request);
			if (result.hasErrors()){
				return VIEW_EDIT;
			}
			return REDIRECT;
		}
    }

	/**
	 * Create a new Resource.
	 * RESTful compliant.
	 * @param resRest
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, headers="content-type=application/xml")
	@ResponseBody
	public RestResource create(@RequestBody RestResource resourceList, HttpServletRequest request) {
		logger.info("Create (RESTful) new Resource");
		try {
			RestResource restList = new RestResource();
			for (ResourceModel res : resourceList.getList()) {
				restList.getList().add(this.save(res, null, request));
			}
			return restList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error("resource.error.default.message", null);
		}
	}
	
	/**
	 * Update a Resource
	 * Browser compliant.
	 * @param familyFilter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.PUT)
	public String update(@Valid @ModelAttribute("resource") ResourceModel resModel, Errors result, ModelMap model, HttpServletRequest request) {
		logger.info("Update Resource " + resModel.getId());
		if (result.hasErrors()){
			result.reject("error.validation.constraints.Default.message");
			this.initModel(resModel, model);
			return VIEW_EDIT;
		} else {
			this.save(resModel, result, request);
			if (result.hasErrors()){
				return VIEW_EDIT;
			} else {
				return REDIRECT;
			}
		}
    }

	/**
	 * Update a family filter
	 * RESTful compliant.
	 * @param familyFilter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.PUT, headers="content-type=application/xml")
	@ResponseBody
	public RestResource update(@RequestBody RestResource resourceList, HttpServletRequest request) {
		try {
			RestResource restList = new RestResource();
			for (ResourceModel res : resourceList.getList()) {
				logger.info("Update (RESTful) resource " + res.getId());
				restList.getList().add(this.save(res, null, request));
			}
			return restList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error("resource.error.default.message", null);
		}

	}

	/**
	 * Delete a Resource, according to an id.
	 * RESTful/Browser compliant.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id, ModelMap model) {
		logger.info("Delete Resource " + id);
		try {
			Resource resourceToDelete  = resourceDao.find(id);
			if (resourceToDelete != null){
				this.deleteFiles(resourceToDelete);
				resourceDao.delete(resourceToDelete);
				model.addAttribute("rest", new RestResource());
			} else {
				this.error("resource.error.AssertNull.message", model);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.error("resource.error.default.message", model);
		}
	    return REDIRECT;
    }
	
	/**
	 * Redirect to the create view.
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/create", method = RequestMethod.GET)
    public String createView(ModelMap model) {		
		logger.info("Show Resource create view");
		this.initModel(new Resource(), model, null);
        return VIEW_EDIT;
    }

	private ResourceModel save(ResourceModel resModel, Errors result, HttpServletRequest request) {
		Resource resource = null;
		if (resModel.getId() == null) {
			resource = new Resource();
		} else {
			resource = this.resourceDao.find(resModel.getId());
			if (resModel.isNewFile()) {
				this.deleteFiles(resource);
			}
			resModel.setTechnicalDatas(resource.getTechnicalDatas());
		}
		List<Filter> filters = this.filterDao.findAll();
		resModel.toResource(resource,filters);
		resource = resourceDao.save(resource);
		if (resModel.isNewFile()) {
			this.saveFiles(resource, resModel, result);
		}
		return new ResourceModel(resource, filters, ResourceUtil.getContextPath(request));
	}
	
	/**
	 * @param resource
	 * @param fileToUpload
	 */
	private void saveFiles(Resource resource, ResourceModel resourceModel, Errors result){
		String fileSeparator = System.getProperty("file.separator");
		
		String pathname = ResourceUtil.getPath(Calendar.getInstance());
		
		// Create repository ?
		File directory = new File(rootname + pathname);		
		if (!directory.exists()){
			if (!directory.mkdirs()) {
				logger.warn("Cannot create repository " + directory.getAbsolutePath());
				result.reject("resource.error.file.save.message");
				return;
			}
		}			
		
		// Save new file
		if (resourceModel.isUploadedFile()) {
			CommonsMultipartFile fileToUpload = resourceModel.getFileData();
			String filename = ResourceUtil.getFilename(resource, fileToUpload.getOriginalFilename());
			File file = new File(rootname + pathname, filename);
			
			fileUploadUtil(file, fileToUpload);
			if (file.exists()) {
				resource.setFileUrl(pathname + fileSeparator + filename);
				this.makeResourceInfo(resource, fileToUpload.getContentType(), fileToUpload.getOriginalFilename());
			} else {
				logger.warn("Cannot create file " + file.getAbsolutePath());
				result.reject("resource.error.file.save.message");
			}
		} else {
			RestFile restFile = resourceModel.getBinaryFile();
			String filename = ResourceUtil.getFilename(resource, restFile.getFilename());
			File file = new File(rootname + pathname, filename);
			
			fileUploadUtil(file, restFile.getBinary());
			if (file.exists()) {
				resource.setFileUrl(pathname + fileSeparator + filename);
				this.makeResourceInfo(resource, restFile.getContentType(), restFile.getFilename());
			} else {
				logger.warn("Cannot create file " + file.getAbsolutePath());
				result.reject("resource.error.file.save.message");
			}
		}
		
		// Update urls
		this.resourceDao.save(resource);
	}
	
	private void deleteFiles(Resource resource) {

		File file = null;
		// File
		if (resource.getFileUrl() != null) {
			file = new File(rootname, resource.getFileUrl());
			if (file.exists()) {
				file.delete();
			}
		}
		// Thumbnail
		if (resource.getThumbnailUrl() != null) {
			file = new File(rootname, resource.getThumbnailUrl());
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	/**
	 * @param file
	 * @param fileToUpload
	 */
	private void fileUploadUtil(File file, CommonsMultipartFile fileToUpload) {
		
		// Creation du flux de lecture
		InputStream inputStream = null;
		// Creation du flux en ecriture
		FileOutputStream outputstream = null;
		
		try {
			inputStream = fileToUpload.getInputStream();
			outputstream = new FileOutputStream(file);
			
			int readByte = inputStream.read();
			while (readByte != -1){
				outputstream.write(readByte);
				readByte = inputStream.read();
			}
			outputstream.flush();
			outputstream.close();
			inputStream.close();			
		
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
		
	/**
	 * @param file
	 * @param fileToUpload
	 */
	private void fileUploadUtil(File file, byte[] bytes) {
		// Creation du flux en ecriture
		FileOutputStream outputstream = null;
		try {
			outputstream = new FileOutputStream(file);
			outputstream.write(bytes);
			outputstream.flush();
			outputstream.close();
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}
		
	public void makeResourceInfo(Resource resource, String contentType, String filename){
		String audioContentType = "audio";
		String imageContentType = "image";
		String videoContentType = "video";	
		ResourceCreationHandler handler = null;
		
		try {
			if (StringUtils.isNotBlank(contentType)){
				if (contentType.startsWith(audioContentType)){
					handler = new AudioCreationHandler(rootname);
				} else if (contentType.startsWith(imageContentType)){
					handler = new ImageCreationHandler(rootname);
				} else if (contentType.startsWith(videoContentType)){
					handler = new VideoCreationHandler(rootname);
				}
				
				if (handler != null){
					handler.onCreate(resource);
				} else {
					resource.setThumbnailUrl(null);
				}
			}
			
		} catch (Throwable e) {
			logger.error(e);
		}
	}

}
