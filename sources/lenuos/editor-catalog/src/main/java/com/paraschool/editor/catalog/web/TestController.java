package com.paraschool.editor.catalog.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paraschool.editor.catalog.dao.TestDao;
import com.paraschool.editor.catalog.models.Test;

@Controller
@RequestMapping("/test")
public class TestController {

	static final Log logger = LogFactory.getLog(TestController.class);
	
	@Resource
	TestDao testDao;
	
	@RequestMapping(value="/list", method= RequestMethod.GET)
	public ModelMap list() {
		for(int i = 0; i  < 10; i++){
			Test test = new Test();
			test.setName("test "+ i);
			testDao.save(test);
		}
		List<Test> tests = testDao.findAll();
		ModelMap map = new ModelMap();
		map.addAttribute("tests", tests);
		return map;
	}
	
	@RequestMapping(value="/new")
	public String create() {
		Test test = new Test();
		test.setName("test "+new Date());
		testDao.save(test);
		return "redirect:list";
	}
	
	@RequestMapping(value="/{id}")
	public String view(ModelMap map, @PathVariable("id") Test test) {
		map.addAttribute("model", test);
		return "test/detail";
	}
}
