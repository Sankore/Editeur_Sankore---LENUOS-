package com.paraschool.editor.server.demo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDSDemo {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), new Configuration());
		Path root = new Path("test/");
		FileStatus[] files = fs.listStatus(root);
		for(FileStatus f : files) {
			System.out.println(f.getPath());
		}
	}

}
