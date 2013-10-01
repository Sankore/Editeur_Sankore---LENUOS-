package com.paraschool.editor.catalog.web.converter;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;

import com.paraschool.editor.catalog.dao.TestDao;
import com.paraschool.editor.catalog.models.Test;

public class TestConverter implements Converter<String, Test> {

	@Resource
	TestDao testDao;

	public Test convert(String source) {
		try{      
			return testDao.find(Integer.parseInt(source));     
		}catch (NumberFormatException e) {
			return null;
		}
	}

}
