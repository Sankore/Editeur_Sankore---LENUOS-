package com.paraschool.editor.client;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.editor.shared.SampleDetails;

public final class AppUtil {
	
	public final static String systemMetaKey() {
		return Window.Navigator.getPlatform().startsWith("Mac") ? "cmd" : "ctrl";
	}
	
	public final static boolean isMeta(NativeEvent event) {
		boolean modifier = false;
		if(Window.Navigator.getPlatform().startsWith("Mac")) {
			modifier = event.getMetaKey();
		}else{
			modifier = event.getCtrlKey();
		}
		return modifier;
	}
	
	public final static String makeProjectRequestHashUrl(ProjectDetails details) {
		return details.getId()+(details.getPath() == null ? "" : "!"+details.getPath())+(details.getLocale() == null ? "" : "&plocale="+details.getLocale());
	}
	
	public final static String makeInProjectURL(ProjectDetails details, String relativeURL) {
		if(relativeURL == null) return null;
		return URL.encode("inproject/"+relativeURL+"?project="+makeProjectRequestHashUrl(details));
	}
	
	public final static String makeExportURL(ProjectDetails details, String relativeURL) {
		if(relativeURL == null) return null;
		return URL.encode("exports/"+relativeURL+"?project="+makeProjectRequestHashUrl(details));
	}
	
	public final static String makeInTemplateURL(TemplateDetails template, String relativeURL) {
		if(relativeURL == null) return null;
		return URL.encode("templates/"+template.getId()+"/"+relativeURL);
	}
	
	public final static String makeSampleURL(SampleDetails sample) {
		String locale = sample.getLocale();
		if(locale != null)
			locale = "_"+locale;
		else
			locale = "";
		
		return URL.encode("samples/"+sample.getId()+locale+"/"+sample.getThumbnail());
	}
	
	public static void bindEnterKeyInTextbox(final TextBoxBase box, final Button button) {
		box.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER)
					button.click();
			}
		});
	}
}
