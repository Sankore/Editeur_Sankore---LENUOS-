package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.api.client.event.ModuleObjectChangeEvent;
import com.paraschool.editor.api.client.event.ModuleObjectDeleteEvent;
import com.paraschool.editor.api.client.event.ModuleObjectEventHandler;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.event.EditMediaEvent;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;

/*
 * Created at 29 sept. 2010
 * By bathily
 */
public class EditableMedia implements ValueChangeHandler<ModuleObject> {

	private final EventBus eventBus;
	private final EditModuleContext context;
	private final MediaContainer media;
	private final Button button;
	private final EditMediaControls controls;
	private final Widget defaultWidget;
	private final ModuleObject.Type[] types;
	
	private final ModuleObjectEventHandler objectHandler = new ModuleObjectEventHandler() {
		
		@Override
		public void onDelete(ModuleObjectDeleteEvent event) {
			setObject(null);
		}
		
		@Override
		public void onChange(ModuleObjectChangeEvent event) {
			setObject(event.getObject());
		}
	};
	
	public EditableMedia(EventBus eventBus, EditModuleContext context, 
			MediaContainer media, Button button, 
			Widget defaultWidget, ModuleObject.Type ... types) {
		
		this.eventBus = eventBus;
		this.context = context;
		this.media = media;
		this.button = button;
		this.controls = new EditMediaControls();
		this.defaultWidget = defaultWidget;
		this.types = types;
		
		init();
		
		if(button != null) button.setEnabled(ModuleUtils.canAddOrRemoveContent(context));
		controls.change.setEnabled(ModuleUtils.canChangeContent(context));
		controls.delete.setEnabled(ModuleUtils.canAddOrRemoveContent(context));
	}

	private void init() {
		
		setControlsVisible(false);
		
		media.addValueChangeHandler(this);
		
		controls.change.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				context.editObject(new ObjectEditCallback() {
					@Override
					public void onEdit(ModuleObject result) {
						if(result != null)
							setObject(result);
					}
				}, types);
			}
		});
		
		controls.delete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setObject(null);
			}
		});
		
		media.insert(controls, 0);
		
		initButton();
	}
	
	private void initButton() {
		if(button != null) {
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					controls.change.click();
				}
			});
		}
	}
	
	private void setControlsVisible(boolean visible) {
		controls.setVisible(visible);
		if(button != null)
			button.setVisible(!visible);
	}
	
	private void cleanPreviousMedia() {
		ModuleObject previous = media.getObject();
		if(previous != null)
			context.removeModuleObjectHandler(previous.getId());
	}
	
	private void setObject(ModuleObject object) {
		media.setMedia(object, defaultWidget);
	}

	public EditMediaControls getControls() {
		return controls;
	}


	@Override
	public void onValueChange(ValueChangeEvent<ModuleObject> event) {
		cleanPreviousMedia();
		
		ModuleObject object = event.getValue();
		if(object != null) {
			context.addModuleObjectHandler(object.getId(), objectHandler);
		}
		
		setControlsVisible((object != null || defaultWidget != null));
		
		eventBus.fireEvent(new EditMediaEvent(media, object));
	}
}
