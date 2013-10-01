package com.paraschool.editor.client.presenter.factory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.paraschool.editor.client.RpcAction;
import com.paraschool.editor.client.RpcAttempt;

public interface RpcActionFactory {
	<T> RpcAction<T> create(AsyncCallback<T> callBack, RpcAttempt<T> attempt);
}
