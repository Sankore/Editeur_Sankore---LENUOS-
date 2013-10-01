package com.paraschool.editor.client.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.inject.Inject;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.ResourceUtils;
import com.paraschool.commons.share.StringUtils;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;
import com.paraschool.editor.shared.LocaleDTO;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

public class EditProjectFormView extends ProjectFormView implements com.paraschool.editor.client.presenter.ProjectPresenter.ProjectFormDisplay {

	private static EditProjectFormViewUiBinder uiBinder = GWT.create(EditProjectFormViewUiBinder.class);
	interface EditProjectFormViewUiBinder extends UiBinder<Widget, EditProjectFormView> {}
	
	interface Resource extends AppResources {
		Resource INSTANCE = GWT.create(Resource.class);
		@Source("images/blank_page.png") ImageResource blankPage();
	}
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String locale();
		String remove();
		String empty();
	}
	
	@UiField Label idLabel;
	@UiField Anchor gipAnchor;
	
	@UiField FormPanel form;
	@UiField FileUpload file;
	@UiField Hidden id;
	@UiField Hidden path;
	@UiField Image icon;
	
	@UiField InlineLabel localeInfo;
	@UiField HTMLList projectLocales;
	@UiField InlineLabel defaultProjectAccess;
	@UiField ListBox locales;
	@UiField Button newLocaleButton;
	@UiField Button changeLocaleButton;
	@UiField Button deleteLocaleButton;
	@UiField HTMLPanel availableLocales;
	@UiField HTMLPanel projectLocalesWrapper;
	
	@UiField HasClickHandlers export;
	@UiField HasClickHandlers clean;
	
	@UiField CssResource css;
	
	@Inject private RpcActionFactory rpcActionFactory;
	@Inject private ProjectServiceAsync projectServiceAsync;
	@Inject private LocalizableResource ressources;
	@Inject private AppMessages messages;
	
	private ProjectDetails details;
	private String selectedLocale;
	private Map<String, HTMLListItem> localesMap = new HashMap<String, HTMLListItem>();
	private String iconUrl;
	
	@Inject
	private EditProjectFormView() {
		initWidget(uiBinder.createAndBindUi(this));
		initIconForm();
		
		ClickHandler cancelLinkAction = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
			}
		};
		
		export.addClickHandler(cancelLinkAction);
		clean.addClickHandler(cancelLinkAction);
		
		ValueChangeHandler<String> emptyChangeHandler = new ValueChangeHandler<String>() {		
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				TextBoxBase textBox = ((TextBoxBase)event.getSource());
				if(textBox.getValue().trim().length() > 0)
					textBox.removeStyleName(css.empty());
				else
					textBox.addStyleName(css.empty());
			}
		};
		
		nameBox.addValueChangeHandler(emptyChangeHandler);
		objectifBox.addValueChangeHandler(emptyChangeHandler);
		descriptionBox.addValueChangeHandler(emptyChangeHandler);
	}
	
	private void initIconForm() {
		file.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				form.submit();
			}
		});
		
		form.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				String filename = file.getFilename(); 
				String type = ResourceUtils.getMimeType(filename);
				
				if(type == null || !ResourceUtils.isImage(type)){
					event.cancel();
					Window.alert(type == null ? messages.uploadFileUnknow() : messages.uploadFileInvalid(type));
					return;
				}
			}
		});
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				try{
					String result = StringUtils.unescapeHTML(event.getResults(), 0);
					Document root = XMLParser.parse(result);
					
					String status = ((Element)root.getFirstChild()).getAttribute("status");
					if("ok".equals(status)){
						NodeList list = root.getFirstChild().getChildNodes();
						for(int i=0 ; i < list.getLength() ; i++) {
							Node node = list.item(i);
							String url = node.getFirstChild().getNodeValue(); 
							if(url != null)
								setIconUrl(url);
						}
						
					}else{
						int errorCode = -1;
						try{
							errorCode = Integer.parseInt(((Element)root.getElementsByTagName("code").item(0)).getFirstChild().getNodeValue());
						}catch (NumberFormatException e) {}
						String values = ((Element)root.getElementsByTagName("values").item(0)).getFirstChild().getNodeValue();
						displayError(errorCode, values);
					}
					
				}catch (Throwable e) {
					e.printStackTrace();
				}
			}
			
		});
	}

	private void displayError(int code, String values) {
		String message = messages.unknowUploadError();
		switch (code) {
		case 0:
			long size = 0;
			try{
				size = Long.parseLong(values);
			}catch (NumberFormatException e) {}
			message = messages.uploadSizeExceededError(size/1024000);
			break;
		default:
			break;
		}
		Window.alert(message);
	}
	
	
	public HasText getLabel() {
		return idLabel;
	}

	@Override
	public void setDetails(ProjectDetails details) {
		super.setDetails(details);
		this.details = details;
		
		getLabel().setText(details.getId());
		gipAnchor.setHref(gipAnchor.getHref()+details.getId());
		
		id.setValue(details.getId());
		
		if(details.getPath() != null)
			path.setValue(details.getPath());
		
		if(details.getIcon() != null)
			icon.setUrl(AppUtil.makeInProjectURL(details, details.getIcon()));
		
		icon.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				icon.setUrl(Resource.INSTANCE.blankPage().getURL());
			}
		});
		
		defaultProjectAccess.addClickHandler(selectLocaleClickHandler(null, changeLocaleButton));
	}

	@UiHandler("change")
	protected void toogleForm(ClickEvent event) {
		form.reset();
		form.setVisible(!form.isVisible());
	}
	
	@UiHandler("delete")
	protected void removeIcon(ClickEvent event) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Boolean result) {
					setIconUrl(null);
			}
		};
		rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>() {
			@Override
			public void call(AsyncCallback<Boolean> callback) {
				projectServiceAsync.removeIcon(getDetails(), callback);
			}
		}).attempt();
	}

	@Override
	public ProjectDetails getDetails() {
		ProjectDetails details = super.getDetails();
		details.setId(idLabel.getText());
		details.setIcon(iconUrl);
		return details;
	}
	
	public void setIconUrl(String url) {
		iconUrl = url;
		form.setVisible(false);
		if(url != null)
			icon.setUrl(AppUtil.makeInProjectURL(getDetails(), url)+"&cache="+new Date().getTime());
		else
			icon.setUrl(Resource.INSTANCE.blankPage().getURL());
	}

	@Override
	public String getLocaleForCreation() {
		return locales.getValue(locales.getSelectedIndex());
	}

	@Override
	public Button createProjectLocaleButton() {
		return newLocaleButton;
	}

	@Override
	public void setProjectLocales(List<LocaleDTO> locales) {
		projectLocales.clear();
		String currentLocale = details.getLocale();
		defaultProjectAccess.setVisible(currentLocale != null);
		
		
		if(locales != null && locales.size() != 0){
			localeInfo.setText(ressources.projectHasLocale());
			for(final LocaleDTO locale : locales) {
				if(!locale.getCode().equals(currentLocale)) {
					HTMLListItem item = new HTMLListItem();
					Button b = new Button(locale.getDisplayName());
					b.setStyleName(css.locale());
					b.addClickHandler(selectLocaleClickHandler(locale.getCode(), changeLocaleButton));
					item.add(b);
					Button d = new Button();
					d.setStyleName(css.remove());
					d.addClickHandler(selectLocaleClickHandler(locale.getCode(), deleteLocaleButton));
					item.add(d);
					projectLocales.add(item);
					localesMap.put(locale.getCode(), item);
				}
			}
			projectLocalesWrapper.setVisible(localesMap.size() > 0);
		}else{
			localeInfo.setText(ressources.noLocaleInProject());
		}
	}
	
	private ClickHandler selectLocaleClickHandler(final String locale, final Button button) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectedLocale = locale;
				button.click();
			}
		};
	}

	@Override
	public String getLocaleForChange() {
		return selectedLocale;
	}

	@Override
	public Button changeProjectLocaleButton() {
		return changeLocaleButton;
	}

	@Override
	public String getLocaleForDeletion() {
		return selectedLocale;
	}

	@Override
	public Button deleteProjectLocaleButton() {
		return deleteLocaleButton;
	}

	@Override
	public void removeLocale(String locale) {
		HTMLListItem item = localesMap.get(locale);
		if(item != null) {
			projectLocales.remove(item);
			localesMap.remove(locale);
		}
		projectLocalesWrapper.setVisible(localesMap.size() > 0);
	}

	@Override
	public void setAvailableLocales(List<LocaleDTO> locales) {
		availableLocales.setVisible(locales != null && locales.size() > 0);
		for(LocaleDTO locale : locales){
			this.locales.addItem(locale.getDisplayName(), locale.getCode());
		}
	}

	@Override
	public HasClickHandlers exportAllPagesButton() {
		return export;
	}

	@Override
	public HasClickHandlers cleanButton() {
		return clean;
	}
	
	
	
}
