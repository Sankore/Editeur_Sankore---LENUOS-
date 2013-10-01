package com.paraschool.commons.share;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("video")
public class VideoResource extends Resource implements HasInfo<VideoInfo>, IsFile {

	private VideoInfo info;

	public VideoResource() {
		super();
	}

	public VideoResource(String id, String url, String name, long size,
			String mimetype, String thumbnail, String description, String locale, VideoInfo info) {
		super(id, url, name, size, mimetype, thumbnail, description, locale);
		this.info = info;
	}

	public VideoInfo getInfo() {
		return info;
	}

	public void setInfo(VideoInfo info) {
		this.info = info;
	}

}
