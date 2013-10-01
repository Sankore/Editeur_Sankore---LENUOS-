package com.paraschool.editor.server.demo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

import com.paraschool.editor.server.thumbnail.WebPageThumbnailBuilder;

/**
 * Used to image web page at a provided URL.
 * 
 * @author Terry Longrie
 */
public class HTMLImager 
{
	public static void main(String[] args) {
		File out = new File("/Users/dbathily/Desktop/web-thumbnail.png");
		try {
			if(out.exists()) out.delete();
			out.createNewFile();
			IOUtils.copy(new WebPageThumbnailBuilder("http://www.google.com/").make(null), new FileOutputStream(out));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}