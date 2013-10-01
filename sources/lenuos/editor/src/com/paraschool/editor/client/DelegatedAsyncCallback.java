package com.paraschool.editor.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * Created at 24 juil. 2010
 * By Didier Bathily
 */
public class DelegatedAsyncCallback<T> implements AsyncCallback<T> {

	protected AjaxHandler handler;
	protected AsyncCallback<T> delegate;
	
	public DelegatedAsyncCallback() {}
	
	public DelegatedAsyncCallback(AjaxHandler handler, AsyncCallback<T> asyncCallBack) {
		this.handler = handler;
		this.delegate = asyncCallBack;
		handler.onStart();
	}
	
	public void onFailure(Throwable caught) {
		handler.onComplete();
		handler.onError(caught);
		this.delegate.onFailure(caught);
	}

	public void onSuccess(T result) {
		handler.onComplete();
		handler.onSuccess();
		this.delegate.onSuccess(result);
	}

}
