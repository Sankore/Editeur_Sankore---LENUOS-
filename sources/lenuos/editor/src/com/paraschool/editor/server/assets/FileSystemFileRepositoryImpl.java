package com.paraschool.editor.server.assets;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileSystemFileRepositoryImpl implements FileRepository {

	private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.	
	
	@Override
	public String separator() {
		return File.separator;
	}

	@Override
	public void putAsset(String path, String assetName, InputStream asset, long lenght) throws Exception {
		createNewDir(path);
		File file = new File(path, assetName);
		if(!file.exists()) file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		try{
			IOUtils.copy(asset, fos);
		}catch (Exception e) {
			IOUtils.closeQuietly(fos);
		}
	}
	
	private void createNewDir(String path) {
		File dir = new File(path);
		dir.mkdirs();
	}
	
	@Override
	public FileStream getAsset(String path) throws FileNotFoundException {
		return getAsset(new File(path));
	}

	@Override
	public FileStream getAssetByName(String path, String name) throws FileNotFoundException{	
		return getAsset(new File(path, name));
	}
	
	private FileStream getAsset(File file) throws FileNotFoundException {
		if (!file.exists())
			throw new FileNotFoundException(file.getAbsolutePath());
		return new FileStream(file.getName(), file.getAbsolutePath(), new FileInputStream(file), file.length(), file.lastModified());
	}
	
	@Override
	public OutputStream getAssetOutputStream(String path, String name) throws FileNotFoundException {
		createNewDir(path);
		return getAssetOutputStream(new File(path, name));
	}
	
	/*
	@Override
	public OutputStream getAssetOutputStream(String path)
			throws FileNotFoundException {
		createNewDir(path);
		return getAssetOutputStream((new File(path)));
	}
	*/
	
	private OutputStream getAssetOutputStream(File file) throws FileNotFoundException {
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new FileNotFoundException();
			}
		return new FileOutputStream(file);
	}

	@Override
	public List<String> getAssetList(String path) {
		List<String> assets = new ArrayList<String>();	
		File dir = new File(path);
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file:files) {
				assets.add(file.getName());
			}
		}
		
		return assets;
	}
	
	/*
	@Override
	public List<FileStream> getAssetStreamList(String path) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		return filesToStreams(files);
	}
	*/
	
	private List<FileStream> filesToStreams(File[] files) {
		List<FileStream> assets = new ArrayList<FileStream>();
		if (files != null) {
			for (File file:files) {
				assets.add(new FileStream(file.getName(), file.getAbsolutePath(), null, file.length(), file.lastModified()));
			}
		}
		return assets;
	}

	@Override
	public boolean exist(String path, String name) {
		return new File(path, name).exists();
	}

	@Override
	public boolean delete(String path, String name) {
		return new File(path, name).delete();
	}

	@Override
	public boolean deleteDirectory(String path) {
		try {
			FileUtils.deleteDirectory(new File(path));
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public long getLastModified(String path, String name)
			throws FileNotFoundException {
		return new File(path, name).lastModified();
	}
	
	@Override
	public void copy(final String source, final String destination, FileFilter filter) throws IOException {
		copy(source, new File(destination), filter);
		/*FileUtils.copyDirectory(new File(source), new File(destination),new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String path = pathname.getAbsolutePath();
				logger.debug(pathname);
				if(path.equals(source+"preview.xml")
						|| path.startsWith(source+File.separator+"save-")
						|| path.startsWith(source+File.separator+exportDirectory)
						|| path.startsWith(getResourceThumbnailDirectory().getAbsolutePath()))
					return false;
				return true;
			}
		});
		*/
	}
	
	@Override
	public void copy(String source, File destination, FileFilter filter) throws IOException {
		if(filter == null)
			FileUtils.copyDirectory(new File(source), destination);
		else
			FileUtils.copyDirectory(new File(source), destination, filter);
	}

	@Override
	public void serveFile(FileStream stream, OutputStream output, long start, long length) throws IOException{
		RandomAccessFile input = new RandomAccessFile(stream.getPath(), "r");
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        
        try{
        	if (input.length() == length) {
                // Write full range.
                while ((read = input.read(buffer)) > 0) {
                    output.write(buffer, 0, read);
                }
            } else {
                // Write partial range.
                input.seek(start);
                long toRead = length;

                while ((read = input.read(buffer)) > 0) {
                    if ((toRead -= read) > 0) {
                        output.write(buffer, 0, read);
                    } else {
                        output.write(buffer, 0, (int) toRead + read);
                        break;
                    }
                }
            }
        }finally {
        	input.close();
		}
	}
	
	@Override
	public List<FileStream> getProjects(final String path, final String projectFile) {
		File[] files = new File(path).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() && new File(pathname, projectFile).exists();
			}
		});
		return filesToStreams(files);
	}

	@Override
	public List<String> getLocales(final String projectDirectory, final String projectFilePattern) {
		File directory = new File(projectDirectory);
		String[] files = directory.list(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return Pattern.matches(projectFilePattern, name);
							}
						});
		ArrayList<String> locales = new ArrayList<String>(files.length-1);
		for(String s : files) {
			Matcher m = Pattern.compile(projectFilePattern).matcher(s);
			m.matches();
			String match = m.group(1); 
			if(match != null && match.length() > 1) {
				if(match.length() > 1 )
					locales.add(match.substring(1));
			}
			
		}
		return locales;
	}
	
	
	
	
}
