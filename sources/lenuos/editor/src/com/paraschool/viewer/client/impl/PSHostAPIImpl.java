package com.paraschool.viewer.client.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.paraschool.viewer.client.HostAPI.Data;

public class PSHostAPIImpl implements HostAPIImpl {
	private JavaScriptObject item;
	
	public PSHostAPIImpl() {
		init();
	}
	
	private native void init() /*-{
		var Item = null;
		function findItem(win)
		{
			while ((win.Item == null) && (win.parent != null) && (win.parent != win))
			{
				win = win.parent;
			}
			Item = win.Item;
		}
		
		findItem($wnd);
		
		if (Item == null && $wnd.opener != null)
		{
			findItem($wnd.opener);
		}
		this.@com.paraschool.viewer.client.impl.PSHostAPIImpl::item = Item;
	}-*/;
	
	
	@Override
	public native Data getSave() /*-{
		return this.@com.paraschool.viewer.client.impl.PSHostAPIImpl::item.getSave();
	}-*/;

	@Override
	public native void finish(Double score, Double time, String data) /*-{
		this.@com.paraschool.viewer.client.impl.PSHostAPIImpl::item.finish(score,time,data);
	}-*/;

	@Override
	public native void save(Double score, Double time, String data) /*-{
		this.@com.paraschool.viewer.client.impl.PSHostAPIImpl::item.save(score,time,data);
	}-*/;

	@Override
	public native void close() /*-{
		this.@com.paraschool.viewer.client.impl.PSHostAPIImpl::item.close();
	}-*/;

}
