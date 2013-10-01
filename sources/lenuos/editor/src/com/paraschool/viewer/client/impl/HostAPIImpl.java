package com.paraschool.viewer.client.impl;

import com.paraschool.viewer.client.HostAPI.Data;

public interface HostAPIImpl {
	
	Data getSave();
	void finish(Double score, Double time, String data);
	void save(Double score, Double time, String data);
	void close();
}