package com.paraschool.editor.modules.commons.client.options;

import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;

public class Option {
	
	private class SetSelectedChoiceOptionChangeCallback implements OptionChangeCallback {
		@Override
		public void onChange(boolean activted, OptionChoice choice) {
			selectedChoice = activted ? choice : null;
		}
	}
	
	private final String id;
	private final String name;
	private final List<OptionChoice> choices;
	private final OptionChangeCallback changeCallback;
	private OptionChoice selectedChoice;
	
	public Option(String name, List<OptionChoice> choices) {
		this.id = HTMLPanel.createUniqueId();
		this.name = name;
		this.choices = choices;
		this.changeCallback = new SetSelectedChoiceOptionChangeCallback();
	}

	public Option(String name, List<OptionChoice> choices,
			OptionChangeCallback changeCallback) {
		this.id = HTMLPanel.createUniqueId();
		this.name = name;
		this.choices = choices;
		this.changeCallback = changeCallback;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public List<OptionChoice> getChoices() {
		return choices;
	}

	public OptionChangeCallback getChangeCallback() {
		return changeCallback;
	}

	public void setSelectedChoice(OptionChoice selectedChoice) {
		this.selectedChoice = selectedChoice;
	}

	public OptionChoice getSelectedChoice() {
		return selectedChoice;
	}
	
	public OptionJSO getJSO() {
		OptionJSO jso = OptionJSO.createObject().cast();
		jso.setName(name);
		if(selectedChoice == null)
			jso.setValue(null);
		else
			jso.setValue(selectedChoice.getValue());
		return jso;
	}
	
	public void updateByJSO(OptionJSO jso) {
		selectedChoice = null;
		for(OptionChoice choice : choices)
			if(choice.getValue().equals(jso.getValue())){
				selectedChoice = choice;
				break;
			}
	}
}
