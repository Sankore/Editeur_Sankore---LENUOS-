package com.paraschool.editor.server.assets;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class HDFSFileRepositoryImpl implements FileRepository {

	private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.

	private final FileSystem fs;

	@Inject
	private HDFSFileRepositoryImpl(@Named("fs.name") String base, Configuration configuration) throws IOException, URISyntaxException {
		fs = FileSystem.get(new URI(base), configuration);
	}

	public FileSystem getFs() {
		return fs;
	}
	
	@Override
	public String separator() {
		return "/";
	}

	@Override
	public void putAsset(String path, String assetName, InputStream asset, long lenght) throws Exception {
		Path p = new Path(path, assetName);
		FSDataOutputStream outputStream = fs.create(p, true);
		try{
			IOUtils.copy(asset, outputStream);
		}finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	@Override
	public List<String> getAssetList(String path) {
		Path p = new Path(path);
		List<String> files = new ArrayList<String>();
		try {
			FileStatus[] status = fs.listStatus(p);
			CollectionUtils.collect(Arrays.asList(status), 
					TransformerUtils.chainedTransformer(TransformerUtils.invokerTransformer("getPath"), TransformerUtils.invokerTransformer("getName")),
					files);// status[0].getPath().getName()
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}


	private List<FileStream> statusToStreams(FileStatus[] status) {
		List<FileStream> files = new ArrayList<FileStream>();
		for(FileStatus s : status) {
			files.add(new FileStream(s.getPath().getName(), s.getPath().toUri().toString(), null, s.getLen(), s.getModificationTime()));
		}
		return files;
	}
	
	/*
	@Override
	public List<FileStream> getAssetStreamList(String path) {
		Path p = new Path(path);
		try {
			return statusToStreams(fs.listStatus(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<FileStream>(0);
	}
	*/

	@Override
	public FileStream getAsset(String path) throws FileNotFoundException {
		return getAsset(new Path(path));
	}

	@Override
	public FileStream getAssetByName(String path, String name)
	throws FileNotFoundException {
		return getAsset(new Path(path,name));
	}

	private FileStream getAsset(Path p) throws FileNotFoundException {
		try {
			FileStatus status = fs.getFileStatus(p);
			FSDataInputStream stream = fs.open(p);
			return new FileStream(p.getName(), p.toUri().toString(), stream, status.getLen(), status.getModificationTime());
		} catch (IOException e) {
			throw new FileNotFoundException(e.getMessage());
		}
	}

	@Override
	public OutputStream getAssetOutputStream(String path, String name)
	throws FileNotFoundException {
		return getAssetOutputStream(new Path(path, name));		
	}

	/*
	@Override
	public OutputStream getAssetOutputStream(String path)
	throws FileNotFoundException {
		return getAssetOutputStream(new Path(path));
	}
	*/

	private OutputStream getAssetOutputStream(Path p)
	throws FileNotFoundException {
		FSDataOutputStream outputStream;
		try {
			outputStream = fs.create(p, true);
		} catch (IOException e) {
			throw new FileNotFoundException(p.getName());
		}
		return outputStream;
	}

	@Override
	public boolean exist(String path, String name) {
		Path p = new Path(path,name);
		try {
			return fs.exists(p);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(String path, String name) {
		Path p = new Path(path,name);
		try {
			return fs.delete(p, true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteDirectory(String path) {
		Path p = new Path(path);
		try {
			return fs.delete(p, true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void copy(String source, String destination, final FileFilter filter) throws IOException {
		
		PathFilter pathFilter = new PathFilter() {
			@Override
			public boolean accept(Path p) {
				return filter.accept(new File(p.toUri()));
			}
		};
		
		FileStatus[] status = fs.listStatus(new Path(source), pathFilter);
		FileUtil.copy(fs, FileUtil.stat2Paths(status), fs, new Path(destination), false, true, fs.getConf());
	}
	
	@Override
	//FIXME la copie vers le filesystem ne se fait pas. Problème de copy de dossier?
	public void copy(String source, File destination, final FileFilter filter) throws IOException {
		Path p = new Path(source);
		PathFilter pathFilter = new PathFilter() {
			@Override
			public boolean accept(Path p) {
				return filter.accept(new File(p.toUri()));
			}
		};
		
		FileStatus[] status = fs.listStatus(p, pathFilter);
		Path pathDestination = new Path(destination.getAbsolutePath());
		for(FileStatus s : status) 
			fs.copyToLocalFile(s.getPath(), pathDestination);
	}

	@Override
	public long getLastModified(String path, String name)
	throws FileNotFoundException {
		Path p = new Path(path,name);
		try {
			return fs.getFileStatus(p).getModificationTime();
		} catch (IOException e) {
			throw new FileNotFoundException(path+"/"+name);
		}
	}

	@Override
	public void serveFile(FileStream stream, OutputStream output, long start,
			long length) throws IOException {

		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int read;
		FSDataInputStream input = (FSDataInputStream)stream.getInputStream();
		try{
			if (stream.getSize() == length) {
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
			IOUtils.closeQuietly(input);
		}
	}

	@Override
	public List<FileStream> getProjects(final String path, final String projectFile) {
		try {
			FileStatus[] status = fs.listStatus(new Path(path), new PathFilter() {
				@Override
				public boolean accept(Path p) {
					try {
						return fs.getFileStatus(p).isDir() && fs.exists(new Path(p, projectFile));
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			});
			return statusToStreams(status);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getLocales(final String projectDirectory, final String projectFilePattern) {
		Path p = new Path(projectDirectory);
		FileStatus[] files;
		try {
			files = fs.listStatus(p,new PathFilter() {
				@Override
				public boolean accept(Path arg0) {
					return Pattern.matches(projectFilePattern, arg0.getName());
				}
			});
			ArrayList<String> locales = new ArrayList<String>(files.length-1);
			for(FileStatus s : files) {
				Matcher m = Pattern.compile(projectFilePattern).matcher(s.getPath().getName());
				m.matches();
				String match = m.group(1); 
				if(match != null && match.length() > 1) {
					if(match.length() > 1 )
						locales.add(match.substring(1));
				}
			}
			return locales;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}




}
