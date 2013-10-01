package com.paraschool.commons.share;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*
 * Created at 28 août 2010
 * By bathily
 */
@SuppressWarnings("serial")
public class SWFInfo implements Serializable, Info, HasWidth, HasHeight {

	@XStreamAsAttribute
	private int width;
	@XStreamAsAttribute
	private int height;
	@XStreamAsAttribute
	private int version;
	@XStreamAsAttribute
	private int frame;
	@XStreamAsAttribute
	private float rate;
	
	@XStreamAsAttribute
	private boolean as3;
	@XStreamAsAttribute
	private boolean network;
	@XStreamAsAttribute
	private boolean gpu;
	
	public SWFInfo() {}
	
	public SWFInfo(int width, int height, int version, int frame,
			float rate, boolean as3, boolean network, boolean gpu) {
		super();
		this.width = width;
		this.height = height;
		this.version = version;
		this.frame = frame;
		this.rate = rate;
		this.as3 = as3;
		this.network = network;
		this.gpu = gpu;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getFrame() {
		return frame;
	}
	public void setFrame(int frameCount) {
		this.frame = frameCount;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float frameRate) {
		this.rate = frameRate;
	}
	public boolean getAs3() {
		return as3;
	}
	public void setAs3(boolean hasAS3) {
		this.as3 = hasAS3;
	}
	public boolean getNetwork() {
		return network;
	}
	public void setNetwork(boolean useNetwork) {
		this.network = useNetwork;
	}
	public boolean getGpu() {
		return gpu;
	}
	public void setGpu(boolean useGPU) {
		this.gpu = useGPU;
	}
	
	
	
}
