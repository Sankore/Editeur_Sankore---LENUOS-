package com.paraschool.editor.server.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Zip {

	private static final Log logger = LogFactory.getLog(Zip.class);
	
	public static void addEntry(ZipArchiveOutputStream zipOutput, File file, FileFilter filter) throws IOException {
		addEntry(zipOutput, file, "", filter);
	}

	public static void addEntry(ZipArchiveOutputStream zipOutput, File file, String folder, FileFilter filter)
	throws IOException {
		folder = ("".equals(folder)?"":folder+File.separator);
		if(!file.isDirectory()){
			ZipArchiveEntry entry = (ZipArchiveEntry)zipOutput.createArchiveEntry(file, folder+file.getName());
			zipOutput.putArchiveEntry(entry);
			InputStream input = new FileInputStream(file); 
			IOUtils.copy(input, zipOutput);
			input.close();
			zipOutput.closeArchiveEntry();
		}else{
			File[] files = file.listFiles(filter);
			for(int i = 0 ; i < files.length ; i++)
				addEntry(zipOutput, files[i],folder+file.getName(), filter);
		}
	}
	
	public static void unzip(InputStream zipInputstream, File destination) throws ZipException, IOException, ArchiveException {
		
		logger.debug("Unzip file in ["+destination.getAbsolutePath()+"]");
		ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", zipInputstream);
		ArchiveEntry entry ;
		while((entry = in.getNextEntry()) != null){
			logger.debug("Find entry ["+entry.getName()+"]");
			
			File file = new File(destination, entry.getName());
			file.getParentFile().mkdirs();
			
			if (!entry.isDirectory()) {
				logger.debug("Entry is not a directory. Write it!");
				OutputStream out = new FileOutputStream(file);
				IOUtils.copy(in, out);
				org.apache.commons.io.IOUtils.closeQuietly(out);
			}else{
				logger.debug("Entry is a directory. Create it!");
				file.mkdir();
			}
					
		}
		in.close();
	}
	
	public static void zip(File directory, OutputStream destination) throws IOException {
		ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(destination);
		File[] files = directory.listFiles();
		for(int i = 0 ; i < files.length ; i++)
			addEntry(zipOutput, files[i], null);
		
		zipOutput.finish();
		zipOutput.close();
	}
}
