package com.paraschool.commons.share;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*
 * Created at 25 août 2010
 * By bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("info")
public class ImageInfo implements Serializable, Info, HasWidth, HasHeight {

	@XStreamAsAttribute
	private int width;
	@XStreamAsAttribute
	private int height;
	
	public ImageInfo() {
		super();
	}
	
	public ImageInfo(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
