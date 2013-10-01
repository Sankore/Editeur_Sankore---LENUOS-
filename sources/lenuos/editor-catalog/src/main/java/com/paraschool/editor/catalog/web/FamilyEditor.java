package com.paraschool.editor.catalog.web;

import java.beans.PropertyEditorSupport;

import com.paraschool.editor.catalog.dao.FamilyFilterDao;
import com.paraschool.editor.catalog.models.FamilyFilter;

public class FamilyEditor extends PropertyEditorSupport {
		 
	    private final FamilyFilterDao familyFiletrDao;
	 
	    public FamilyEditor(FamilyFilterDao familyFiletrDao) {
	        this.familyFiletrDao = familyFiletrDao;
	    }
	 
	    @Override
	    public void setAsText(String text) throws IllegalArgumentException {
	        setValue(familyFiletrDao.find(Integer.parseInt(text)));
	    }
	 
	    @Override
	    public String getAsText() {
	        FamilyFilter s = (FamilyFilter) getValue();
	        if (s == null) {
	            return null;
	        } else {
	            return s.getId().toString();
	        }
	    }
	}
