package com.paraschool.editor.catalog.web;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paraschool.editor.catalog.dao.FamilyFilterDao;
import com.paraschool.editor.catalog.dao.FilterDao;
import com.paraschool.editor.catalog.models.FamilyFilter;
import com.paraschool.editor.catalog.web.models.rest.RestError;
import com.paraschool.editor.catalog.web.models.rest.RestFamilyFilter;


@Controller
@RequestMapping(FamilyFilterController.requestMapping)
public class FamilyFilterController {
	
	private static final Log logger = LogFactory.getLog(FamilyFilterController.class);
	
	public static final String requestMapping = "/familyFilters";

	private static final String VIEW_LIST = "familyFilters/familyFilterList";
	private static final String VIEW_EDIT = "familyFilters/familyFilterDetail";
	private static final String REDIRECT = "redirect:";
	
	private static final String MODEL_REST = "catalog";
	private static final String MODEL_ELEMENT = "family";
	private static final String MODEL_ELEMENTS = "families";
	private static final String MODEL_ERROR = "error";
	
	@javax.annotation.Resource
	FamilyFilterDao familyFilterDao;
		
	@javax.annotation.Resource
	FilterDao filterDao;
		
	/**
	 * Initialize model's view
	 * @param filter
	 * @param model
	 */
	private void initModel(FamilyFilter family, ModelMap model) {
		RestFamilyFilter restList = new RestFamilyFilter();
		restList.getList().add(family);
		model.addAttribute(MODEL_REST, restList);
		model.addAttribute(MODEL_ELEMENT, family);
	}

	/**
	 * List family filters.
	 * Browser/RESTful compliant.
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String list(ModelMap model) {
		logger.info("Get Family filter list");
		List<FamilyFilter> families = familyFilterDao.findAll();
		RestFamilyFilter restList = new RestFamilyFilter();
		restList.getList().addAll(families);
		model.addAttribute(MODEL_REST, restList);
		model.addAttribute(MODEL_ELEMENTS, families);
		return VIEW_LIST;
	}
	
	/**
	 * Build error data.
	 * @param msgKey
	 * @param model
	 * @return
	 */
	private RestFamilyFilter error(String msgKey, ModelMap model) {
		RestFamilyFilter restList = new RestFamilyFilter();
		restList.setError(new RestError(msgKey));
		if (model != null) {
			model.addAttribute(MODEL_REST, restList);
			model.addAttribute(MODEL_ERROR, restList.getError());
		}
		return restList;
	}
	
	/**
	 * Get a family filter, according to an id.
	 * Browser/RESTful compliant.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String read(@PathVariable("id") Integer id, ModelMap model) {
		logger.info("Get Family filter " + id);
		FamilyFilter family = familyFilterDao.find(id);
		if (family == null) {
			this.error("familyFilter.error.AssertNull.message", model);
			List<FamilyFilter> families = familyFilterDao.findAll();
			model.addAttribute(MODEL_ELEMENTS, families);
			return VIEW_LIST;
		} else {
			this.initModel(family, model);
	        return VIEW_EDIT;
		}
    }
	
	/**
	 * Create a new Family filter.
	 * Browser compliant.
	 * @param familyFilter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute(MODEL_ELEMENT) FamilyFilter familyFilter, Errors result, ModelMap model) {
		logger.info("Create new Family filter");
		if (result.hasErrors()){
			result.reject("error.validation.constraints.Default.message");
			this.initModel(familyFilter, model);
			return VIEW_EDIT;
		} else {
			familyFilterDao.save(familyFilter);
			return REDIRECT;
		}
    }
	/**
	 * Create a new Family filter.
	 * RESTful compliant.
	 * @param familyFilter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, headers="content-type=application/xml")
	@ResponseBody
	public RestFamilyFilter create(@RequestBody RestFamilyFilter familyFilterList) {
		logger.info("Create (RESTful) new Family filter");
		try {
			RestFamilyFilter restList = new RestFamilyFilter();
			for (FamilyFilter ff : familyFilterList.getList()) {
				restList.getList().add(familyFilterDao.save(ff));
			}
			return restList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error("familyFilter.error.default.message", null);
		}
	}	
	/**
	 * Update a family filter
	 * Browser compliant.
	 * @param familyFilter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.PUT)
	public String update(@Valid @ModelAttribute(MODEL_ELEMENT) FamilyFilter familyFilter, Errors result, ModelMap model) {
		logger.info("Update Family filter " + familyFilter.getId());
		if (result.hasErrors()){
			result.reject("error.validation.constraints.Default.message");
			this.initModel(familyFilter, model);
			return VIEW_EDIT;
		} else {
			familyFilterDao.save(familyFilter);
			return REDIRECT;
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
	public RestFamilyFilter update(@RequestBody RestFamilyFilter familyFilterList) {
		try {
			RestFamilyFilter restList = new RestFamilyFilter();
			for (FamilyFilter ff : familyFilterList.getList()) {
				logger.info("Update (RESTful) Family filter " + ff.getId());
				restList.getList().add(familyFilterDao.save(ff));
			}
			return restList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error("familyFilter.error.default.message", null);
		}
    }

	/**
	 * Delete a family filter, according to an id.
	 * RESTful/Browser compliant.
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id, ModelMap model) {
		logger.info("Delete Family filter " + id);
		try {
			FamilyFilter familyFilterToDelete  = familyFilterDao.find(id);
			if (familyFilterToDelete != null){
				if (filterDao.isUsed(familyFilterToDelete)) {
					this.error("familyFilter.error.AssertUsed.message", model);
					List<FamilyFilter> families = familyFilterDao.findAll();
					model.addAttribute(MODEL_ELEMENTS, families);
					return VIEW_LIST;
				} else {
					familyFilterDao.delete(familyFilterToDelete);
					model.addAttribute(MODEL_REST, new RestFamilyFilter());
				}
			} else {
				this.error("familyFilter.error.AssertNull.message", model);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.error("familyFilter.error.default.message", model);
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
		logger.info("Show Family filter create view");
		this.initModel(new FamilyFilter(), model);
        return VIEW_EDIT;
    }
	
}