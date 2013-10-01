package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.TextEditCallback;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.event.EditMediaEvent;

/*
 * Created at 29 sept. 2010
 * By bathily
 */
public class EditableHTML {

	
	private final EventBus eventBus;
	private final EditModuleContext context;
	private final HTML text;
	private final Button button;
	private final TextEditCallback callback;
	
	public EditableHTML(final EventBus eventBus,final EditModuleContext context,final HTML text,final Button button) {
		
		this.eventBus = eventBus;
		this.context = context;
		this.text = text;
		this.button = button;
		
		initButton();
		
		callback = new TextEditCallback() {
			@Override
			public void onEdit() {
				if(button != null) {
					if(text.getText().trim().length() == 0)
						hideText();
				}
				eventBus.fireEvent(new EditMediaEvent(text, null));
			}
		};
		
		if(ModuleUtils.canChangeContent(context)) context.makeEditable(text,callback);
		if(button != null) button.setEnabled(ModuleUtils.canAddOrRemoveContent(context));
	}
	
	private void initButton() {
		if(button != null) {
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					showText();
				}
			});
		}
		update();
	}
	
	public void showText() {
		show(true);
	}
	
	public void hideText() {
		show(false);
	}
	
	private native void setFocus(Element element) /*-{
		element.focus();
	}-*/;
	
	private void show(boolean show) {
		text.setVisible(show);
		if(button != null) button.setVisible(!show);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				setFocus(text.getElement());
			}
		});
	}
	
	public void update() {
		if(text.getText().trim().length() != 0)
			showText();
		else
			hideText();
	}
	
}
