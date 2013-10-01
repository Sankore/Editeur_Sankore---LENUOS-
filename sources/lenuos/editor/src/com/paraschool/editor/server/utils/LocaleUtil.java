package com.paraschool.editor.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Created at 28 sept. 2010
 * By bathily
 */
public final class LocaleUtil {

	/**
	 * Convert a string based locale into a Locale Object.
	 * Assumes the string has form "{language}_{country}_{variant}".
	 * Examples: "en", "de_DE", "_GB", "en_US_WIN", "de__POSIX", "fr_MAC"
	 *  
	 * @param localeString The String
	 * @return the Locale
	 */
	public static Locale getLocaleFromString(String localeString)
	{
		if (localeString == null)
		{
			return null;
		}
		localeString = localeString.trim();
		if (localeString.toLowerCase().equals("default"))
		{
			return Locale.getDefault();
		}

		// Extract language
		int languageIndex = localeString.indexOf('_');
		String language = null;
		if (languageIndex == -1)
		{
			// No further "_" so is "{language}" only
			return new Locale(localeString, "");
		}
		else
		{
			language = localeString.substring(0, languageIndex);
		}

		// Extract country
		int countryIndex = localeString.indexOf('_', languageIndex + 1);
		String country = null;
		if (countryIndex == -1)
		{
			// No further "_" so is "{language}_{country}"
			country = localeString.substring(languageIndex+1);
			return new Locale(language, country);
		}
		else
		{
			// Assume all remaining is the variant so is "{language}_{country}_{variant}"
			country = localeString.substring(languageIndex+1, countryIndex);
			String variant = localeString.substring(countryIndex+1);
			return new Locale(language, country, variant);
		}
	}

	public static List<Locale> getCandidateLocales(Locale locale) {

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		List<Locale> locales = new ArrayList<Locale>(4);
		if (variant.length() > 0) {
			locales.add(locale);
		}
		if (country.length() > 0) {
			locales.add((locales.size() == 0) ?
					locale : new Locale(language, country, ""));
		}
		if (language.length() > 0) {
			locales.add((locales.size() == 0) ?
					locale : new Locale(language, "", ""));
		}
		locales.add(Locale.ROOT);
		return locales;
	}
}
