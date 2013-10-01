package com.paraschool.editor.server.demo;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieTag;

/*
 * Created at 28 août 2010
 * By bathily
 */
public class SWFDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Movie movie = new Movie();
		try {
			movie.decodeFromFile(new File("/Users/bathily/Desktop/test.swf"));
			for(MovieTag tag : movie.getObjects()){
				System.out.println(tag);
			}
		} catch (DataFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
