package com.paraschool.editor.catalog.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(LoginController.requestMapping)
public class LoginController {

	private static final Log logger = LogFactory.getLog(LoginController.class);
	public static final String requestMapping = "/login";
	private static final String VIEW_EDIT = "login/login";
	
	private static final String MODEL_ERROR = "error";
	
	/**
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String displayView(ModelMap model, HttpServletRequest request) {
		logger.info("Login");
		return VIEW_EDIT;
	}
	
	/**
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/error", method = RequestMethod.GET)
	public String displayErrorView(ModelMap model, HttpServletRequest request) {
		logger.info("Login error");
		model.addAttribute(MODEL_ERROR, "login.badCredentials");
		return VIEW_EDIT;
	}
	
}
