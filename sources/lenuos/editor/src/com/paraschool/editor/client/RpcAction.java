package com.paraschool.editor.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class RpcAction<T> extends DelegatedAsyncCallback<T> {

	public abstract void attempt();
	protected abstract String retryMessage();
	
	protected int retryCount;
	
	RpcAction(AjaxHandler handler, AsyncCallback<T> asyncCallBack) {
		this(0, handler, asyncCallBack);
	}
	
	RpcAction(int retryCount, AjaxHandler handler, AsyncCallback<T> asyncCallBack) {
		super(handler, asyncCallBack);
		this.retryCount = retryCount;
	}

	@Override
	public void onFailure(Throwable caught) {

		if(retryCount > 0){
			if(Window.confirm(retryMessage())) {
				attempt();
				retryCount--;
			}
		}else{
			super.onFailure(caught);
		}
	}
}
