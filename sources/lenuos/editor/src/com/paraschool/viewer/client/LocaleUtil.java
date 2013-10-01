package com.paraschool.viewer.client;

import java.util.ArrayList;
import java.util.List;

/*
 * Created at 28 sept. 2010
 * By bathily
 */
public final class LocaleUtil {

	public static List<String> getCandidateLocales(String locale) {

		String[] splited = locale.split("_");
		
		List<String> locales = new ArrayList<String>(4);
		if(splited.length == 4) {
			locales.add(locale);
		}
		if(splited.length >= 3) {
			locales.add(splited[0]+"_"+splited[1]+"_"+splited[2]);
		}
		if(splited.length >= 2) {
			locales.add(splited[0]+"_"+splited[1]);
		}
		if(splited.length >= 1) {
			locales.add(splited[0]);
		}

		return locales;
	}
}
