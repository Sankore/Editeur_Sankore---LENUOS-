package com.paraschool.editor.shared;

import java.io.Serializable;

public class LocaleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String displayName;
	
	public LocaleDTO() {
		super();
	}
	
	public LocaleDTO(String code, String displayName) {
		super();
		this.code = code;
		this.displayName = displayName;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	

}
