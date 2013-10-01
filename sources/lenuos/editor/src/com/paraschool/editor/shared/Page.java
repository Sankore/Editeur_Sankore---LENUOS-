package com.paraschool.editor.shared;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int current;
	private int totalPageCount;
	private int size;
	private List<T> data;
	
	public Page() {
		super();
	}

	public Page(int current, int totalPageCount, int size, List<T> data) {
		super();
		this.current = current;
		this.totalPageCount = totalPageCount;
		this.size = size;
		this.data = data;
	}
	
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	
	
}
