package com.paraschool.editor.api.client.ui;

import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ui.players.AudioPlayer;
import com.paraschool.editor.api.client.ui.players.VideoPlayer;

/*
 * Created at 30 sept. 2010
 * By bathily
 */
public final class UIUtils {

	
	private UIUtils(){}
	
	private static Widget getFallbackWidget(String name, String url) {
		Anchor a = new Anchor(name, url, "_blank");
		a.addStyleName("fallback");
		return a;
	}
	
	public static Widget getWidgetForObject(ModuleObject object) {
		
		Widget w = null;
		
		String url = GWT.getHostPageBaseURL()+ object.getUrl();
		try {
			if(object.getType().equals(ModuleObject.Type.IMAGE)){
				w = new Image(url);
			}else if(object.getType().equals(ModuleObject.Type.VIDEO)){
				w = new VideoPlayer(url, false, object.getHeight()+"px", object.getWidth() == 0 ? "100%" : object.getWidth()+"px", object.getMimetype());
			}else if(object.getType().equals(ModuleObject.Type.SOUND)){
				w = new AudioPlayer(url, false, object.getMimetype());
			}else if(object.getType().equals(ModuleObject.Type.SWF)){
				/*w = new SWFWidget(url, object.getWidth() == 0 ? "100%" : object.getWidth()+"px", 
						object.getHeight() == 0 ? "100%" : object.getHeight()+"px", new PluginVersion(object.getVersion(), 0, 0));
						*/
				w = new SWFWidget(url, "100%", object.getHeight() == 0 ? "100%" : object.getHeight()+"px", new PluginVersion(object.getVersion(), 0, 0));
				((SWFWidget)w).addProperty("wmode", "opaque");
				
			}else {
				w = new Anchor(object.getName(), object.getUrl(),"_blank");
				w.setStyleName(object.getType().name().toLowerCase());
			}
		} catch (Exception e) {
			GWT.log(e.toString());
			w = getFallbackWidget(object.getName(), url);
		}
		
		w.addStyleName("media");
		
		return w;
	}
}
