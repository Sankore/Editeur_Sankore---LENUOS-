package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChangeCallback;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 4 oct. 2010
 * By bathily
 */
public class OptionWidget extends HTMLListItem {

	interface OptionsWidgetUiBinder extends UiBinder<Widget, OptionWidget> {}
	private static OptionsWidgetUiBinder uiBinder = GWT.create(OptionsWidgetUiBinder.class);
	
	interface Style extends CssResource {
		String option();
		String thumbnail();
		String description();
		String inputContainer();
	}
	
	private final Option option;
	private final int choiceIndex;
	private final boolean isRadio;
	
	@UiField Style style;
	@UiField Image thumbnail;
	@UiField Label description;
	@UiField FlowPanel inputContainer;
	
	public OptionWidget(Option option, int choiceIndex) {
		
		add(uiBinder.createAndBindUi(this));
		addStyleName(style.option());
		
		this.option = option;
		this.choiceIndex = choiceIndex;
		this.isRadio = option.getChoices().size() > 1;
		
		initUI();
	}

	private void initUI() {
		OptionChoice choice = option.getChoices().get(choiceIndex);
		thumbnail.setUrl(choice.getThumbnail().getURL());
		description.setText(choice.getDescription());
		
		CheckBox input = isRadio ? new RadioButton(option.getId()+"-"+option.getName()) : new CheckBox();
		input.setName(option.getId()+"-"+option.getName());
		inputContainer.add(input);
		
		input.setValue(choice.equals(option.getSelectedChoice()));
		
		input.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				CheckBox source = ((CheckBox)event.getSource());
				OptionChangeCallback callback = null;
				if((callback = option.getChangeCallback()) != null)
					callback.onChange(source.getValue(), option.getChoices().get(choiceIndex));
			}
		});
	}
}
