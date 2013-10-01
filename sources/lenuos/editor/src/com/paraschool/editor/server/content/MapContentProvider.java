package com.paraschool.editor.server.content;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paraschool.commons.share.Identifiable;
import com.paraschool.editor.server.ContentExistException;

public class MapContentProvider<T extends Identifiable> implements ContentProvider<T> {

	protected Log logger = LogFactory.getLog(getClass());
	protected HashMap<String, T> contents;
	
	public MapContentProvider() {
		contents = new HashMap<String, T>();
	}
	
	@Override
	public T get(String id) {
		return contents.get(id);
	}

	@Override
	public ArrayList<T> list() {
		return new ArrayList<T>(contents.values());
	}

	@Override
	public void register(String id, T content) {
		if(contents.containsKey(id)) throw new ContentExistException(id);
		contents.put(id, content);	
	}

	@Override
	public void unregister(String id) {
		contents.remove(id);
	}

	@Override
	public InputStream get(T t) {
		return null;
	}

	@Override
	public boolean delete(String id) {
		unregister(id);
		return true;
	}

}
