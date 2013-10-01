package com.paraschool.editor.api.client;

import java.io.Serializable;

/*
 * Created at 15 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
public class ModuleObject implements Serializable{
	
	public enum Type {
		IMAGE, SOUND, VIDEO, SWF, DOCUMENT, LINK, OTHER, ALL
	}
	
	private String id;
	private String name;
	private String url;
	private long size;
	private String mimetype;
	private Type type;
	
	private int width;
	private int height;
	
	private long duration;
	private String codec;
	private String format;
	
	private int version;
	private int frame;
	private float rate;
	private boolean as3;
	private boolean network;
	private boolean gpu;
	
	public ModuleObject() {
		super();
	}
	
	public ModuleObject(String id, String name, String url, long size, String mimetype, Type type) {
		this();
		this.id = id;
		this.setName(name);
		this.url = url;
		this.size = size;
		this.mimetype = mimetype;
		this.type = type;
	}
	
	public ModuleObject(String id, String name, String url, long size, String mimetype, Type type, int width, int height) {
		this(id, name, url, size, mimetype, type);
		this.width = width;
		this.height = height;
	}
	
	public ModuleObject(String id, String name, String url, long size, String mimetype, Type type, int width, int height,
			long duration, String codec, String format) {
		this(id, name, url, size, mimetype, type, width, height);
		this.duration = duration;
		this.codec = codec;
		this.format = format;
	}
	
	public ModuleObject(String id, String name, String url, long size, String mimetype, Type type, int width, int height,
			int version, int frame, float rate, boolean as3, boolean network, boolean gpu) {
		this(id, name, url, size, mimetype, type, width, height);
		this.version = version;
		this.frame = frame;
		this.rate = rate;
		this.as3 = as3;
		this.network = network;
		this.gpu = gpu;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public boolean isAs3() {
		return as3;
	}

	public void setAs3(boolean as3) {
		this.as3 = as3;
	}

	public boolean isNetwork() {
		return network;
	}

	public void setNetwork(boolean network) {
		this.network = network;
	}

	public boolean isGpu() {
		return gpu;
	}

	public void setGpu(boolean gpu) {
		this.gpu = gpu;
	}
	
}
