package com.paraschool.viewer.client.impl;

import com.paraschool.viewer.client.HostAPI.Data;

public class NullHostAPIImpl implements HostAPIImpl {

	@Override
	public Data getSave() {return null;}

	@Override
	public void finish(Double score, Double time, String data) {}

	@Override
	public void save(Double score, Double time, String data) {}

	@Override
	public void close() {}

}
