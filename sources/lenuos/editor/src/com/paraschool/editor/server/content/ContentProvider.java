package com.paraschool.editor.server.content;

import java.io.InputStream;
import java.util.ArrayList;

import com.paraschool.editor.server.ContentExistException;

public interface ContentProvider<T> {

	T get(String id);
	boolean delete(String id);
	InputStream get(T t);
	ArrayList<T> list();
	
	void register(String id, T i) throws ContentExistException;
	void unregister(String id);
	
}
