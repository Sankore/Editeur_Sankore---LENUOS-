package com.paraschool.editor.modules.test.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.api.client.ModuleObject;

/*
 * Created at 30 sept. 2010
 * By bathily
 */
public class Resources extends PopupPanel {

	
	public static final List<ModuleObject> objects = new ArrayList<ModuleObject>();
	
	static {
		objects.add(new ModuleObject("im1", "Le zèbre", "img1.png", 0, "images/png", ModuleObject.Type.IMAGE, 246, 211));
		objects.add(new ModuleObject("im2", "Le chameau", "img2.jpg", 0, "images/jpg", ModuleObject.Type.IMAGE, 2178, 1451));
		objects.add(new ModuleObject("im3", "Flèche", "img3.png", 0, "images/png", ModuleObject.Type.IMAGE, 76, 60));
		objects.add(new ModuleObject("snd1", "Un son très court", "snd1.mp3", 0, "audio/mp3", ModuleObject.Type.SOUND));
		objects.add(new ModuleObject("snd2", "Bob l'éponge", "snd2.mp3", 0, "audio/mp3", ModuleObject.Type.SOUND));
		objects.add(new ModuleObject("vid1", "Un flv", "movie.flv", 0, "video/flv", ModuleObject.Type.VIDEO));
		objects.add(new ModuleObject("vid2", "Un mp4", "movie.mp4", 0, "video/mp4", ModuleObject.Type.VIDEO));
		objects.add(new ModuleObject("swf1", "Un swf", "movie.swf", 0, "application/flash", ModuleObject.Type.SWF));
		objects.add(new ModuleObject("link", "Google", "http://www.google.fr", 0, null, ModuleObject.Type.LINK));
		objects.add(new ModuleObject("document", "A pdf", "data.pdf", 0, null, ModuleObject.Type.DOCUMENT));
	}
	
	private static final FlexTable table = new FlexTable();
	
	public Resources(final ObjectEditCallback callback) {
		super(true, true);
		for(int i = 0 ; i < objects.size() ; i++) {
			final ModuleObject object = objects.get(i);
			table.setWidget(i, 0, new Label(object.getId()));
			table.setWidget(i, 1, new Label(object.getName()));
			table.setWidget(i, 2, new Label(object.getUrl()));
			table.setWidget(i, 3, new Label(object.getMimetype()));
			table.setWidget(i, 4, new Label(object.getType().name()));
			
			Button choose = new Button("Choose");
			choose.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					callback.onEdit(object);
					hide();
				}
			});
			table.setWidget(i, 4, choose);
		}
		add(table);
		show();
		center();
	}
}
