package com.paraschool.commons.share;

import java.io.Serializable;
import java.util.ArrayList;



/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
public class Page implements Serializable{

	private String statement;
	private TemplateDetails templateDetails;
	private ArrayList<Interactivity> interactivities;

	public Page() {
		super();
	}
	
	public Page(TemplateDetails templateDetails) {
		this();
		this.setTemplateDetails(templateDetails);
	}
	
	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getStatement() {
		return statement;
	}

	public void setTemplateDetails(TemplateDetails templateDetails) {
		this.templateDetails = templateDetails;
	}

	public TemplateDetails getTemplateDetails() {
		return templateDetails;
	}
	
	public void setInteractivities(ArrayList<Interactivity> interactivities) {
		this.interactivities = interactivities;
	}

	public ArrayList<Interactivity> getInteractivities() {
		if(interactivities == null)
			interactivities = new ArrayList<Interactivity>();
		return interactivities;
	}
	
	public void addInteractivity(Interactivity i) {
		addInteractivity(i, -1);
	}
	
	public void addInteractivity(Interactivity i, int index) {
		if(index == -1) getInteractivities().add(i);
		else getInteractivities().add(index, i);
	}
	
	public void removeInteractivity(Interactivity i) {
		getInteractivities().remove(i);
	}
	
}
