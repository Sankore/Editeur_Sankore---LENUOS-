package com.paraschool.editor.modules.test.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.EditorModuleDescriptor;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.api.client.EditModuleContext.Mode;
import com.paraschool.editor.interactivity.client.InteractivityView;
import com.paraschool.editor.interactivity.client.PagePanel;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class ModulePanel extends Composite {

	interface ModulePanelUiBinder extends UiBinder<HTMLPanel, ModulePanel> {}
	private static ModulePanelUiBinder uiBinder = GWT.create(ModulePanelUiBinder.class);
	
	
	private final EditorModule module;
	private ModuleWidget currentModuleWidget;
	
	@UiField HTML name;
	@UiField HTML description;
	@UiField HTML author;
	@UiField Image thumbnail;
	
	@UiField TextArea area;
	
	@UiField Panel edit;
	@UiField PagePanel page;
	@UiField Button execute;
	@UiField HTML data;
	@UiField Button executeView;
	@UiField HTML dataView;
	@UiField ListBox modeBox;
	
	public ModulePanel(final EditorModule module, final String data) {
		this.module = module;
		initWidget(uiBinder.createAndBindUi(this));
		setDescription();
		execute.setVisible(false);
		executeView.setVisible(false);
		area.setText(Cookies.getCookie(module.getDescriptor().getId() + "-data"));
	}
	
	private void setDescription() {
		EditorModuleDescriptor descriptor = module.getDescriptor();
		name.setHTML(descriptor.getName()+"("+descriptor.getVersion()+") @ "+descriptor.getFamily());
		description.setHTML(descriptor.getDescription());
		author.setHTML(descriptor.getAutor()+" @ "+descriptor.getCompany());
	}
	
	private Mode modeForString(String modeString) {
		if(modeString.equals("FULL"))
			return Mode.FULL;
		if(modeString.equals("LIGHT"))
			return Mode.LIGHT;
		if(modeString.equals("ONLY_MEDIA"))
			return Mode.ONLY_MEDIA;
		if(modeString.equals("NONE"))
			return Mode.NONE;
		return null;
	}
	
	@UiFactory
	protected Image getThumbnailImage() {
		return new Image(module.getDescriptor().getThumbnail());
	}
	
	private void setModuleWidget(String data) {
		clear(null);
		currentModuleWidget = module.newWidget();
		InteractivityView view = new InteractivityView();
		view.getModuleTitle().setHTML(module.getDescriptor().getName());
		
		EditContext context = new EditContext(data, modeForString(modeBox.getValue(modeBox.getSelectedIndex())));
		view.setCanDelete(context.getMode() == Mode.FULL);
		view.setInteractivityWidget(currentModuleWidget.editWidget(context), currentModuleWidget.optionsWidget(context));
		edit.add(view);
		execute.setVisible(true);
	}
	
	@UiHandler("load")
	protected void load(ClickEvent e) {
		String text = area.getText();
		Cookies.setCookie(module.getDescriptor().getId() + "-data", text);
		setModuleWidget(text);
	}
	
	@UiHandler("clear")
	protected void clear(ClickEvent e) {
		execute.setVisible(false);
		executeView.setVisible(false);
		edit.clear();
		page.clearAllInteractivities();
		data.setHTML((String)null);
	}
	
	@UiHandler("execute")
	protected void execute(ClickEvent e) {
		String data = currentModuleWidget.getEditData(); 
		this.data.setText(data);
		page.clearAllInteractivities();
		page.addInteractivity(module.getDescriptor().getId(), currentModuleWidget.viewWidget(new ViewContext(data)));
		executeView.setVisible(true);
	}
	
	@UiHandler("executeView")
	protected void executeView(ClickEvent e) {
		String data = currentModuleWidget.getResultData(); 
		this.dataView.setText(data);
	}
	
	@UiHandler("data")
	protected void setDataToArea(ClickEvent e) {
		area.setText(((HasText)e.getSource()).getText());
	}
 
}
