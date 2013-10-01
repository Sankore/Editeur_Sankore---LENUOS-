package com.paraschool.editor.catalog.web;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paraschool.editor.catalog.dao.FamilyFilterDao;
import com.paraschool.editor.catalog.dao.FilterDao;
import com.paraschool.editor.catalog.dao.ResourceDao;
import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.models.Filter;
import com.paraschool.editor.catalog.web.models.rest.RestError;
import com.paraschool.editor.catalog.web.models.rest.RestFilter;


@Controller
@RequestMapping(FilterController.requestMapping)
public class FilterController {
	
	private static final Log logger = LogFactory.getLog(FilterController.class);
	
	public static final String requestMapping = "/filters";
	
	private static final String VIEW_LIST = "filters/filterList";
	private static final String VIEW_EDIT = "filters/filterDetail";
	private static final String REDIRECT = "redirect:";
	
	private static final String MODEL_REST = "catalog";
	private static final String MODEL_ELEMENT = "filter";
	private static final String MODEL_ELEMENTS = "filters";
	private static final String MODEL_ERROR = "error";
	private static final String MODEL_FAMILIES = "families";


	@javax.annotation.Resource
	FilterDao filterDao;
	
	@javax.annotation.Resource
	ResourceDao resourceDao;

	@javax.annotation.Resource
	FamilyFilterDao familyFilterDao;	
	
	/**
	 * Initialize model's view
	 * @param filter
	 * @param model
	 */
	private void initModel(Filter filter, ModelMap model) {
		RestFilter restList = new RestFilter();
		restList.getList().add(filter);
		model.addAttribute(MODEL_REST, restList);
		model.addAttribute(MODEL_ELEMENT, filter);
		model.addAttribute(MODEL_FAMILIES, familyFilterDao.findAll());
	}

	/**
	 * List filters.
	 * Browser/RESTful compliant.
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String list(ModelMap model) {
		logger.info("Get Filter list");
		List<Filter> filters = filterDao.findAll();
		RestFilter restList = new RestFilter();
		restList.getList().addAll(filters);
		model.addAttribute(MODEL_REST, restList);
		model.addAttribute(MODEL_ELEMENTS, filters);
		return VIEW_LIST;
	}
	
	/**
	 * Build error data.
	 * @param msgKey
	 * @param model
	 * @return
	 */
	private RestFilter error(String msgKey, ModelMap model) {
		RestFilter restList = new RestFilter();
		restList.setError(new RestError(msgKey));
		if (model != null) {
			model.addAttribute(MODEL_REST, restList);
			model.addAttribute(MODEL_ERROR, restList.getError());
		}
		return restList;
	}
	
	/**
	 * Get a filter, according to an id.
	 * Browser/RESTful compliant.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String read(@PathVariable("id") Integer id, ModelMap model) {
		logger.info("Get filter " + id);
		Filter filter = filterDao.find(id);
		if (filter == null) {
			this.error("filter.error.AssertNull.message", model);
			List<Filter> filters = filterDao.findAll();
			model.addAttribute(MODEL_ELEMENTS, filters);
			return VIEW_LIST;
		} else {
			this.initModel(filter, model);
	        return VIEW_EDIT;
		}
    }
	
	/**
	 * Create a new filter.
	 * Browser compliant.
	 * @param filter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute(MODEL_ELEMENT) Filter filter, Errors result, ModelMap model) {
		logger.info("Create new filter");
		if (result.hasErrors()){
			result.reject("error.validation.constraints.Default.message");
			this.initModel(filter, model);
			return VIEW_EDIT;
		} else {
			this.save(filter);
			return REDIRECT;
		}
    }
	
	/**
	 * Create a new filter.
	 * RESTful compliant.
	 * @param filter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, headers="content-type=application/xml")
	@ResponseBody
	public RestFilter create(@RequestBody RestFilter filterList) {
		logger.info("Create (RESTful) new filter");
		try {
			RestFilter restList = new RestFilter();
			for (Filter f : filterList.getList()) {
				restList.getList().add(this.save(f));
			}
			return restList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error("filter.error.default.message", null);
		}
	}	
	
	/**
	 * Update a filter
	 * Browser compliant.
	 * @param filter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.PUT)
	public String update(@Valid @ModelAttribute(MODEL_ELEMENT) Filter filter, Errors result, ModelMap model) {
		logger.info("Update filter " + filter.getId());
		if (result.hasErrors()){
			result.reject("error.validation.constraints.Default.message");
			this.initModel(filter, model);
			return VIEW_EDIT;
		} else {
			this.save(filter);
			return REDIRECT;
		}
    }
	
	/**
	 * Update a filter
	 * RESTful compliant.
	 * @param filter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.PUT, headers="content-type=application/xml")
	@ResponseBody
	public RestFilter update(@RequestBody RestFilter filterList) {
		try {
			RestFilter restList = new RestFilter();
			for (Filter f : filterList.getList()) {
				logger.info("Update (RESTful) filter " + f.getId());
				restList.getList().add(this.save(f));
			}
			return restList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error("filter.error.default.message", null);
		}
    }

	/**
	 * Save a filter.
	 * @param filter
	 * @return
	 */
	private Filter save(Filter filter) {
		FamilyFilter family = familyFilterDao.find(filter.getFamily().getId());
		filter.setFamily(family);
		return filterDao.save(filter);
	}
	
	/**
	 * Delete a filter, according to an id.
	 * RESTful/Browser compliant.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id, ModelMap model) {
		logger.info("Delete filter " + id);
		try {
			Filter filterToDelete  = filterDao.find(id);
			if (filterToDelete != null){
				if (resourceDao.isUsed(filterToDelete)) {
					this.error("filter.error.AssertUsed.message", model);
					List<Filter> filters = filterDao.findAll();
					model.addAttribute(MODEL_ELEMENTS, filters);
					return VIEW_LIST;
				} else {
					filterDao.delete(filterToDelete);
					model.addAttribute(MODEL_REST, new RestFilter());
				}
			} else {
				this.error("filter.error.AssertNull.message", model);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.error("filter.error.default.message", model);
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
		logger.info("Show filter create view");
		this.initModel(new Filter(), model);
        return VIEW_EDIT;
    }

	/**
	 * Data binder.
	 * @param dataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		// Family filter converter.
	    dataBinder.registerCustomEditor(FamilyFilter.class, new FamilyEditor(familyFilterDao));
	}

}