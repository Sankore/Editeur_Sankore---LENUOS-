package com.paraschool.commons.share;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * Created at 28 août 2010
 * By bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("audio")
public class AudioResource extends Resource implements HasInfo<AudioInfo>, IsFile {

	private AudioInfo info;
	
	public AudioResource() {
		super();
	}
	
	public AudioResource(String id, String url, String name, long size,
			String mimetype, String thumbnail, String description, String locale, AudioInfo info) {
		super(id, url, name, size, mimetype, thumbnail, description, locale);
		this.info = info;
	}

	public AudioInfo getInfo() {
		return info;
	}

	public void setInfo(AudioInfo info) {
		this.info = info;
	}

	
	
}
