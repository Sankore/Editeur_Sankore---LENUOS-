package com.paraschool.commons.share;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@SuppressWarnings("serial")
public class AudioInfo implements Serializable, Info {
	
	@XStreamAsAttribute
	private long duration;
	//MP3
	@XStreamAsAttribute
	private String codec;
	//FMT_S16
	@XStreamAsAttribute
	private String format;

	public AudioInfo() {}
	
	public AudioInfo(long duration, String codec, String format) {
		super();
		this.duration = duration;
		this.codec = codec;
		this.format = format;
	}

	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
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
