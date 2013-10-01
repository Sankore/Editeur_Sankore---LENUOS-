package com.paraschool.viewer.client;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.paraschool.viewer.client.impl.HostAPIImpl;

public class HostAPI {
	
	public static class Data extends JavaScriptObject {
		protected Data() {}
		
		public final native Double getScore() /*-{return this.score;}-*/;
		public final native void setScore(double score) /*-{this.score = score;}-*/;
		
		public final native Double getTime() /*-{return this.time;}-*/;
		public final native void setTime(double time) /*-{this.time = time;}-*/;
		
		public final native String getData() /*-{return this.data;}-*/;
		public final native void setData(double data) /*-{this.data = data;}-*/;
	}
	
	private static final Logger logger = Logger.getLogger(Viewer.class.getName());
	private HostAPIImpl impl = GWT.create(HostAPIImpl.class);
	
	public HostAPI() {
		logger.info("Host ["+impl.getClass().getName()+"] loaded");
	}
	
	protected ArrayList<ArrayList<String>> parseDatas(Data data) {
		ArrayList<ArrayList<String>> saves = null;
		String saveData = data == null ? null : data.getData();
		if(saveData != null) {
			logger.info("Parse save data "+saveData);
			JSONArray json = JSONParser.parseStrict(saveData).isArray();
			if(json != null) {
				saves = new ArrayList<ArrayList<String>>();
				logger.fine("Save data is an array.");
				for(int i=0 ; i<json.size() ; i++) {
					JSONArray pageJson = json.get(i).isArray();
					ArrayList<String> pageData = null;
					if(pageJson != null) {
						pageData = new ArrayList<String>(pageJson.size());
						for(int j=0 ; j<pageJson.size() ; j++) {
							pageData.add(j, pageJson.get(j).toString());
						}
					}
					saves.add(i, pageData);
				}
			}else
				logger.fine("Save data isn't an array.");
		}else{
			logger.fine("Didn't fine any save data");
		}
		return saves;
	}
	
	protected String toData(ArrayList<ArrayList<String>> datas) {
		if(datas != null) {
			logger.info("Converte datas.");
			JsArray<JsArray<JavaScriptObject>> results = JsArray.createArray().cast();
			for(int i=0 ; i<datas.size(); i++) {
				ArrayList<String> pageDatas = datas.get(i);
				if(pageDatas != null) {
					JsArray<JavaScriptObject> pageResults = JsArray.createArray().cast();
					for(int j=0 ; j<pageDatas.size() ; j++){
						String moduleData = pageDatas.get(j);
						logger.info("add data ["+i+","+j+"]"+ moduleData);
						if(moduleData != null) {
							JSONObject json = JSONParser.parseStrict(moduleData).isObject();
							pageResults.set(j, json.getJavaScriptObject());
						}else
							pageResults.set(j, null);
						
					}
					results.set(i, pageResults);
				}
			}
			return results.toString();
		}
		logger.info("There is no data to convert.");
		return null;
	}
	
	public ArrayList<ArrayList<String>> getSaveData() {
		logger.info("get save data");
		return parseDatas(impl.getSave());
	}
	
	public void finish(Double score, Double time, ArrayList<ArrayList<String>> datas) {
		String converted = toData(datas);
		logger.info("finish with score: "+score+", time: "+time+" and data ["+converted+"]");
		impl.finish(score, time, converted);
	}
	
	public void save(Double score, Double time, ArrayList<ArrayList<String>> datas) {
		String converted = toData(datas);
		logger.info("save with score: "+score+", time: "+time+" and data ["+converted+"]");
		impl.save(score, time, converted);
	}
	
	public void close() {
		logger.info("close");
		impl.close();
	}
}
