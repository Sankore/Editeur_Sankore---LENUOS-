package com.paraschool.editor.client;

/*
 * Created at 24 juil. 2010
 * By Didier Bathily
 */
public interface AjaxHandler {
	void onStart();
	void onComplete();
	void onError(Throwable caught);
	void onSuccess();
}
