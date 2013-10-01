package com.paraschool.editor.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcAttempt<T> {
	void call(AsyncCallback<T> callback);
}
