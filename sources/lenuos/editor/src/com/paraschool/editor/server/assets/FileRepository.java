package com.paraschool.editor.server.assets;


import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileRepository {
	
	public void putAsset(String path, String assetName, InputStream asset, long lenght) throws Exception;
	public List<String> getAssetList(String path);
	//public List<FileStream> getAssetStreamList(String path);
	public FileStream getAsset(String path) throws FileNotFoundException;
	public FileStream getAssetByName(String path, String name) throws FileNotFoundException;
	public OutputStream getAssetOutputStream(String path, String name) throws FileNotFoundException;
	public boolean exist(String path, String name);
	public boolean delete(String path, String name);
	public boolean deleteDirectory(String path);
	public void copy(String source, String destination, FileFilter filter)  throws IOException;
	public void copy(String source, File destination, FileFilter filter) throws IOException;
	
	public long getLastModified(String path, String name) throws FileNotFoundException;
	
	public void serveFile(FileStream stream, OutputStream output, long start, long length) throws IOException;
	
	public List<FileStream> getProjects(final String path, final String projectFile);
	public List<String> getLocales(String projectDirectory, String projectFilePattern);
	
	public String separator();
}
