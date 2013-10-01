package com.paraschool.editor.server.utils;

public class UrlUtil {

    /**
     * /** Add a parameter to a URL.
     * 
     * @param sb
     *            Current URL text.
     * @param paramName
     *            Name of parameter to add to URL. Cannot be null or empty.
     * @param paramValue
     *            Value of parameter to add to URL.
     * @throws IllegalArgumentException
     *             If parameter name is null or empty.
     */
    public static void addParameter(StringBuilder sb, String paramName, String paramValue) {
        if (isBlankStr(paramName))
            throw new IllegalArgumentException("URL parameter name cannot be null or empty.");
        sb.append((sb.indexOf("?") < 0) ? '?' : '&');
        sb.append(paramName);
        sb.append('=');
        if (!isBlankStr(paramValue))
            sb.append(paramValue);
    }

    /**
     * /** This function gets a parameter value from a URL by name.
     * 
     * @param url
     *            The URL to get the parameter from.
     * @param paramName
     *            The parameter to get.
     * @return The value for the parameter, or <code>null</code> if there is no such parameter or the parameter name
     *         is null or empty.
     */
    public static String getParameter(String url, String paramName) {
        String parameterValue = null;
        ParamOffsets paramOffsets = getParamOffsets(url, paramName);
        if (paramOffsets != null) {
            if (paramOffsets.valStart < 0)
                parameterValue = "";
            else
                parameterValue = url.substring(paramOffsets.valStart, paramOffsets.valEnd + 1);
        }
        return parameterValue;
    }

    /**
     * Set the value of a URL parameter or append the parameter if it does not exist.
     * 
     * @param url
     *            The URL.
     * @param paramName
     *            Name of the parameter to set.
     * @param value
     *            Value for the parameter.
     * @throws IllegalArgumentException
     *             If parameter name is null or empty.
     * @return URL with parameter value set.
     */
    public static String setParameter(String url, String paramName, String value) {
        if (isBlankStr(paramName))
            throw new IllegalArgumentException("URL parameter name cannot be null or empty.");
        StringBuilder returnURL = new StringBuilder(url);
        ParamOffsets paramOffsets = getParamOffsets(url, paramName);
        if (paramOffsets == null) {
            addParameter(returnURL, paramName, value);
        }
        else {
            if (paramOffsets.valStart < 0) {
                int valStart = paramOffsets.nameStart + paramName.length() + 1;
                returnURL.replace(valStart, valStart, value);
            }
            else
                returnURL.replace(paramOffsets.valStart, paramOffsets.valEnd + 1, value);
        }
        return returnURL.toString();
    }

    /**
     * Remove a parameter from a URL by name.
     * 
     * @param url
     *            The URL to remove the parameter from.
     * @param paramName
     *            The parameter to remove.
     * @throws IllegalArgumentException
     *             If parameter name is null or empty.
     * @return The URL with the parameter removed or the original URL if there is no such parameter.
     */
    public static String removeParameter(String url, String paramName) {
        if (isBlankStr(paramName))
            throw new IllegalArgumentException("URL parameter name cannot be null or empty.");
        ParamOffsets paramOffsets = getParamOffsets(url, paramName);
        if (paramOffsets != null) {
            StringBuffer sb = new StringBuffer(url);
            int paramEnd = paramOffsets.nameStart + paramName.length(); // If param has no value, remove up to and
            // including =
            if (paramOffsets.valStart >= 0)
                paramEnd = paramOffsets.valEnd; // If param has a value, remove up to and include value end.
            int separatorIndex = paramOffsets.nameStart - 1; // Index of ? or & just before param name.
            char separator = sb.charAt(separatorIndex);
            sb.replace(separatorIndex, paramEnd + 1, "");
            if (sb.length() > separatorIndex)
                sb.setCharAt(separatorIndex, separator);
            url = sb.toString();
        }
        return url;
    }

    /**
     * Get the value of an optional integer URL parameter.
     * 
     * @param sourceURL
     *            A URL.
     * @param paramName
     *            Name of the integer parameter to get.
     * @return Value of the integer parameter or null if the parameter does not exist.
     * @throws IllegalArgumentException
     *             If parameter name is null or empty.
     * @throws NumberFormatException
     *             If the parameter exists and is not a valid integer.
     */
    private static Integer getIntParameter(String sourceURL, String paramName) {
        if (isBlankStr(paramName))
            throw new IllegalArgumentException("URL parameter name cannot be null or empty.");
        String curParamValueStr = UrlUtil.getParameter(sourceURL, paramName);
        if (isBlankStr(curParamValueStr))
            return null;
        return Integer.parseInt(curParamValueStr);
    }

    /**
     * Increment a URL integer parameter by 1.
     * 
     * @param sourceURL
     *            URL.
     * @param paramName
     *            Parameter to increment.
     * @return URL with incremented parameter.
     * @throws IllegalArgumentException
     *             If parameter name is null or empty, or URL has no such parameter.
     * @throws NumberFormatException
     *             If URL parameter is blank or not an integer.
     */
    public static String incrementIntParameter(String sourceURL, String paramName) {
        return incrementIntParameter(sourceURL, paramName, 1);
    }

    /**
     * Increment a URL integer parameter.
     * 
     * @param sourceURL
     *            A URL with an integer parameter.
     * @param paramName
     *            Parameter to increment.
     * @param delta
     *            Value to increment by.
     * @return URL with adjusted parameter.
     * @throws IllegalArgumentException
     *             If parameter name is null or empty, or URL has no such parameter.
     * @throws NumberFormatException
     *             If URL parameter is blank or not an integer.
     */
    public static String incrementIntParameter(String sourceURL, String paramName, int delta) {
        if (isBlankStr(paramName))
            throw new IllegalArgumentException("URL parameter name cannot be null or empty.");
        Integer curParamValue = getIntParameter(sourceURL, paramName);
        if (curParamValue == null)
            throw new IllegalArgumentException("URL did not contain expected parameter.");
        Integer newParamValue = curParamValue + delta;
        return UrlUtil.setParameter(sourceURL, paramName, newParamValue.toString());
    }

    /**
     * URL parameter name / value offsets.
     */
    private static class ParamOffsets {
        int nameStart = -1;
        int valStart = -1;
        int valEnd = -1;
    }

    /**
     * Get parameter name and value offsets within a URL.
     * 
     * @param url
     *            The URL to get the parameter offsets within.
     * @param paramName
     *            The parameter to get offsets for.
     * 
     * @return The name and value offsets within the URL for the parameter, or <code>null</code> if there is no such
     *         parameter or the parameter name is null or empty.
     * @throws IllegalArgumentException
     *             If parameter name is null or empty.
     */
    private static ParamOffsets getParamOffsets(String url, String paramName) {
        if (isBlankStr(paramName))
            throw new IllegalArgumentException("URL parameter name cannot be null or empty.");
        int lastCharIndex = url.length() - 1;
        char paramSeparator = '?';
        int startIndex = 0;
        while (startIndex <= lastCharIndex) {
            int nextParamIndex = url.indexOf(paramSeparator, startIndex);
            if (nextParamIndex < 0)
                return null;
            int paramNameIndex = nextParamIndex + 1;
            int charAfterParamNameIndex = paramNameIndex + paramName.length();
            if (charAfterParamNameIndex > lastCharIndex)
                return null;
            if ((url.charAt(charAfterParamNameIndex) == '=')
                    && url.regionMatches(paramNameIndex, paramName, 0, paramName.length())) {
                ParamOffsets paramOffsets = new ParamOffsets();
                paramOffsets.nameStart = paramNameIndex;
                int paramValueIndex = charAfterParamNameIndex + 1;
                if ((paramValueIndex <= lastCharIndex) && (url.charAt(paramValueIndex) != '&')) {
                    paramOffsets.valStart = paramValueIndex;
                    nextParamIndex = url.indexOf('&', paramValueIndex);
                    if (nextParamIndex == -1)
                        paramOffsets.valEnd = url.length() - 1;
                    else
                        paramOffsets.valEnd = nextParamIndex - 1;
                }
                return paramOffsets;
            }
            paramSeparator = '&';
            startIndex = paramNameIndex;
        }
        return null;
    }

    private static boolean isBlankStr(String str) {
        return ((str == null) || (str.length() == 0));
    }
}
