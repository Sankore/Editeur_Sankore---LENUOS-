package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.ResourceUploadPresenter;

/*
 * Created at 19 juil. 2010
 * By Didier Bathily
 */
public class ResourceUpload extends CompositeDisplayView implements ResourceUploadPresenter.Display {

	private static ResourceUploadUiBinder uiBinder = GWT.create(ResourceUploadUiBinder.class);
	interface ResourceUploadUiBinder extends UiBinder<FlowPanel, ResourceUpload> {}
	
	@UiField Hidden id;
	@UiField Hidden path;
	@UiField Hidden locale;
	
	@UiField Label progress;
	@UiField FileUpload file;
	@UiField FormPanel form;
	
	final AppMessages messages;
	
	@Inject
	private ResourceUpload() {
		initWidget(uiBinder.createAndBindUi(this));
		this.messages = GWT.create(AppMessages.class);
		setFinish();
	}

	@Override
	public FileUpload getFile() {
		return file;
	}

	@Override
	public FormPanel getForm() {
		return form;
	}

	@Override
	public void setProgress(int progress) {
		this.progress.setText(progress+"%");
		
	}

	@Override
	public void setFinish() {
		reset();
	}

	@Override
	public void setStart() {
		this.form.setVisible(false);
		this.progress.setVisible(true);
	}

	@Override
	public void setError() {
		this.progress.setText(messages.uploadHasError());
		new Timer() {
			@Override
			public void run() {
				reset();
			}
		}.schedule(5000);
	}

	@Override
	public void reset() {
		this.form.reset();
		this.progress.setText(messages.uploadInProgress());
		this.progress.setVisible(false);
		this.form.setVisible(true);
	}

	@Override
	public Hidden getId() {
		return id;
	}

	@Override
	public Hidden getPath() {
		return path;
	}

	@Override
	public Hidden getLocale() {
		return locale;
	}

}
