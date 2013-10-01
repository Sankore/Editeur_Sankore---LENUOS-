package com.paraschool.commons.share;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@SuppressWarnings("serial")
public class VideoInfo implements Serializable, Info, HasHeight, HasWidth {
	
	@XStreamAsAttribute
	private long duration;
	@XStreamAsAttribute
	private int width;
	@XStreamAsAttribute
	private int height;

	//H264
	@XStreamAsAttribute
	private String codec;
	//YUV420P
	@XStreamAsAttribute
	private String format;

	public VideoInfo() {}
	
	public VideoInfo(long duration, int width, int height, String codec,
			String format) {
		super();
		this.duration = duration;
		this.width = width;
		this.height = height;
		this.codec = codec;
		this.format = format;
	}

	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
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
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
}
