package com.paraschool.editor.shared;

import java.io.Serializable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Author;

@SuppressWarnings("serial")
public class Parameters implements Serializable {
	
	private Boolean connected = false;
	private Author author;
	
	@Inject @Named("security.retrieve.login") private Boolean canRetrieveLogin;
	@Inject @Named("security.retrieve.login.external.service.url") private String retrieveLoginExternalServiceUrl;
	
	@Inject @Named("opensankore.sso.enable") private Boolean isOpenSankoreSSOEnabled;
	@Inject @Named("opensankore.sso.quiturl") private String openSankoreSSOExitUrl;
	
	@Inject @Named("publish.allowed") private Boolean canPublish;
	@Inject @Named("can.upload.model") private Boolean canUploadModel;
	
	@Inject @Named("use.inview.logging") private Boolean useInViewLogging;
	@Inject @Named("use.remote.logging") private Boolean useRemoteLogging;
	
	@Inject @Named("application.version") private String applicationVersion;
	@Inject @Named("application.build") private String applicationBuild;
	@Inject @Named("application.buildDate") private String applicationBuildDate;
	
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Boolean getCanRetrieveLogin() {
		return canRetrieveLogin;
	}
	public void setCanRetrieveLogin(Boolean canRetrieveLogin) {
		this.canRetrieveLogin = canRetrieveLogin;
	}
	public String getRetrieveLoginExternalServiceUrl() {
		return retrieveLoginExternalServiceUrl;
	}
	public void setRetrieveLoginExternalServiceUrl(
			String retrieveLoginExternalServiceUrl) {
		this.retrieveLoginExternalServiceUrl = retrieveLoginExternalServiceUrl;
	}
	public Boolean getIsOpenSankoreSSOEnabled() {
		return isOpenSankoreSSOEnabled;
	}
	public void setIsOpenSankoreSSOEnabled(Boolean isOpenSankoreSSOEnabled) {
		this.isOpenSankoreSSOEnabled = isOpenSankoreSSOEnabled;
	}
	public String getOpenSankoreSSOExitUrl() {
		return openSankoreSSOExitUrl;
	}
	public void setOpenSankoreSSOExitUrl(String openSankoreSSOExitUrl) {
		this.openSankoreSSOExitUrl = openSankoreSSOExitUrl;
	}
	public void setCanPublish(Boolean canPublish) {
		this.canPublish = canPublish;
	}
	public Boolean getCanPublish() {
		return canPublish;
	}
	
	public void setCanUploadModel(Boolean canUploadModel) {
		this.canUploadModel = canUploadModel;
	}
	public Boolean getCanUploadModel() {
		return canUploadModel;
	}
	public void setConnected(Boolean connected) {
		this.connected = connected;
	}
	public Boolean getConnected() {
		return connected;
	}
	public Boolean getUseInViewLogging() {
		return useInViewLogging;
	}
	public void setUseInViewLogging(Boolean useInViewLogging) {
		this.useInViewLogging = useInViewLogging;
	}
	public Boolean getUseRemoteLogging() {
		return useRemoteLogging;
	}
	public void setUseRemoteLogging(Boolean useRemoteLogging) {
		this.useRemoteLogging = useRemoteLogging;
	}
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}
	public String getApplicationVersion() {
		return applicationVersion;
	}
	public void setApplicationBuild(String applicationBuild) {
		this.applicationBuild = applicationBuild;
	}
	public String getApplicationBuild() {
		return applicationBuild;
	}
	public void setApplicationBuildDate(String applicationBuildDate) {
		this.applicationBuildDate = applicationBuildDate;
	}
	public String getApplicationBuildDate() {
		return applicationBuildDate;
	}
}
