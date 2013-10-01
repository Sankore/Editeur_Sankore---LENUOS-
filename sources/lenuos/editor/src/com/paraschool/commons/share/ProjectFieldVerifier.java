package com.paraschool.commons.share;

public class ProjectFieldVerifier {

	private static boolean notEmpty(String value) {
		if (value == null) {
			return false;
		}
		return value.trim().length() != 0;
	}
	
	public static boolean isValidProjectDetail(ProjectDetails details) {
		return notEmpty(details.getName()) /*&& notEmpty(details.getObjectif()) 
			&& notEmpty(details.getDescription()) */&& notEmpty(details.getVersion());
	}
}
