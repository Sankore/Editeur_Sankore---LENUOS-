package com.paraschool.editor.modules.test.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.TextEditCallback;

/*
 * Created at 27 oct. 2010
 * By bathily
 */
public final class AlohaTextEditor {

	private static Map<Widget, TextEditCallback> widgets = new HashMap<Widget, TextEditCallback>();
	static {
		bind();
	}
	
	private AlohaTextEditor() {}
	
	private static native void bind() /*-{
		
		var f = function(event, eventProperties){
			@com.paraschool.editor.modules.test.client.AlohaTextEditor::onEdit(Lcom/google/gwt/dom/client/Element;Z)(eventProperties.editable.obj[0], eventProperties.editable.isModified());
		};
			
		$wnd.GENTICS.Aloha.EventRegistry.subscribe($wnd.GENTICS.Aloha, "editableDeactivated", f);
	}-*/;
	
	private static native void edit(Element element) /*-{
		$wnd.$(element).aloha();
	}-*/;
	
	private static void onEdit(Element element, boolean isModified) {
		if(isModified) {
			for(Entry<Widget, TextEditCallback> entry : widgets.entrySet()) {
				if(entry.getKey().getElement().equals(element)) {
					TextEditCallback callback = entry.getValue();
					if(callback != null)
						callback.onEdit();
					return;
				}
			}
		}
		
	}
	
	public static void edit(Widget widget, TextEditCallback callback) {
		widgets.put(widget, callback);
		edit(widget.getElement());
	}
}
