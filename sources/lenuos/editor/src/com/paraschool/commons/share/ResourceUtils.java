package com.paraschool.commons.share;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResourceUtils {
	private static HashMap<String, String> allMimeTypes = new HashMap<String, String>();
	private static HashMap<String, String> imageMimeTypes = new HashMap<String, String>();
	private static HashMap<String, String> audioMimeTypes = new HashMap<String, String>();
	private static HashMap<String, String> videoMimeTypes = new HashMap<String, String>();
	private static HashMap<String, String> documentMimeTypes = new HashMap<String, String>();
	private static HashMap<String, String> zippableMimeTypes = new HashMap<String, String>();
	private static HashMap<String, String> flashMimeTypes = new HashMap<String, String>();
	
	static {
		videoMimeTypes.put("mp4", "video/mp4");
		videoMimeTypes.put("mp2", "video/mpeg");
		videoMimeTypes.put("mpa", "video/mpeg");
		videoMimeTypes.put("mpe", "video/mpeg");
		videoMimeTypes.put("mpeg", "video/mpeg");
		videoMimeTypes.put("mpa", "video/mpeg");
		videoMimeTypes.put("mpv2", "video/mpeg");
		videoMimeTypes.put("mov", "video/quicktime");
		videoMimeTypes.put("qt", "video/quicktime");
		videoMimeTypes.put("lsf", "video/x-la-asf");
		videoMimeTypes.put("lsx", "video/x-la-asf");
		videoMimeTypes.put("asf", "video/x-ms-asf");
		videoMimeTypes.put("asr", "video/x-ms-asf");
		videoMimeTypes.put("asx", "video/x-ms-asf");
		videoMimeTypes.put("avi", "video/x-msvideo");
		videoMimeTypes.put("movie", "video/x-sgi-movie");
		videoMimeTypes.put("flv", "video/x-flv");
		
		audioMimeTypes.put("au", "audio/basic");
		audioMimeTypes.put("snd", "audio/basic");
		audioMimeTypes.put("mid", "audio/mid");
		audioMimeTypes.put("rmi", "audio/mid");
		audioMimeTypes.put("mp3", "audio/mpeg");
		audioMimeTypes.put("aif", "audio/x-aiff");
		audioMimeTypes.put("aifc", "audio/x-aiff");
		audioMimeTypes.put("aiff", "audio/x-aiff");
		audioMimeTypes.put("m3u", "audio/x-mpegurl");
		audioMimeTypes.put("ra", "audio/x-pn-realaudio");
		audioMimeTypes.put("ram", "audio/x-pn-realaudio");
		audioMimeTypes.put("wav", "audio/x-wav");
		
		imageMimeTypes.put("bmp", "image/bmp");
		imageMimeTypes.put("cod", "image/cis-cod");
		imageMimeTypes.put("gif", "image/gif");
		imageMimeTypes.put("ief", "image/ief");
		imageMimeTypes.put("jpe", "image/jpeg");
		imageMimeTypes.put("jpeg", "image/jpeg");
		imageMimeTypes.put("jpg", "image/jpeg");
		imageMimeTypes.put("jfif", "image/pipeg");
		imageMimeTypes.put("png", "image/png");
		imageMimeTypes.put("svg", "image/svg+xml");
		imageMimeTypes.put("tiff", "image/tiff");
		imageMimeTypes.put("ras", "image/x-cmu-raster");
		imageMimeTypes.put("cmx", "image/x-cmx");
		imageMimeTypes.put("ico", "image/x-icon");
		imageMimeTypes.put("pnm", "image/x-portable-anymap");
		imageMimeTypes.put("pbm", "image/x-portable-bitmap");
		imageMimeTypes.put("pgm", "image/x-portable-graymap");
		imageMimeTypes.put("ppm", "image/x-portable-pixmap");
		imageMimeTypes.put("rgb", "image/x-rgb");
		imageMimeTypes.put("xbm", "image/x-xbitmap");
		imageMimeTypes.put("xpm", "image/x-xpixmap");
		imageMimeTypes.put("xwd", "image/x-xwindowdump");
		
		documentMimeTypes.put("pdf", "application/pdf");
		documentMimeTypes.put("doc", "application/msword");
		documentMimeTypes.put("docx", "application/msword");
		documentMimeTypes.put("xls", "application/msexcel");
		documentMimeTypes.put("xlsx", "application/msexcel");
		documentMimeTypes.put("ppt", "application/mspowerpoint");
		documentMimeTypes.put("pptx", "application/mspowerpoint");
		documentMimeTypes.put("pages", "application/x-iwork-pages-sffpages");
		documentMimeTypes.put("numbers", "application/x-iwork-numbers-sffnumbers");
		documentMimeTypes.put("key", "application/x-iwork-keynote-sffkeynote");
		
		documentMimeTypes.put("odb", "application/vnd.oasis.opendocument.database");
		documentMimeTypes.put("odc", "application/vnd.oasis.opendocument.chart");
		documentMimeTypes.put("odf", "application/vnd.oasis.opendocument.formula");
		documentMimeTypes.put("odp", "application/vnd.oasis.opendocument.presentation");
		documentMimeTypes.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		documentMimeTypes.put("odt", "application/vnd.oasis.opendocument.text");
		
		documentMimeTypes.put("csv", "text/csv");
		documentMimeTypes.put("html", "text/html");
		documentMimeTypes.put("txt", "text/plain");
		documentMimeTypes.put("rtf", "text/rtf");
		documentMimeTypes.put("xml", "text/xml");
		
		zippableMimeTypes.put("zip", "application/zip");
		zippableMimeTypes.put("wgt", "application/widget");
		
		flashMimeTypes.put("swf", "application/x-shockwave-flash");
		
		allMimeTypes.putAll(imageMimeTypes);
		allMimeTypes.putAll(videoMimeTypes);
		allMimeTypes.putAll(audioMimeTypes);
		allMimeTypes.putAll(documentMimeTypes);
		allMimeTypes.putAll(zippableMimeTypes);
		allMimeTypes.putAll(flashMimeTypes);
		
		allMimeTypes.put("xml", "application/xml");
		
	}
	
	public static Collection<String> getValidExtensions() {
		return allMimeTypes.values();
	}
	
	public static Map<String, String> getAllMimeTypes() {
		return allMimeTypes;
	}
	
	public static String getMimeType(String filename) {
		String ext = filename.substring(filename.lastIndexOf(".")+1);
		if(ext != null)
			return allMimeTypes.get(ext.toLowerCase());
		return null;
	}
	
	public static boolean isImage(String mimeType) {
		return imageMimeTypes.values().contains(mimeType);
	}
	
	public static boolean isVideo(String mimeType) {
		return videoMimeTypes.values().contains(mimeType);
	}
	
	public static boolean isAudio(String mimeType) {
		return audioMimeTypes.values().contains(mimeType);
	}
	
	public static boolean isPDF(String mimeType) {
		return "application/pdf".equals(mimeType);
	}
	
	public static boolean isSWF(String mimeType) {
		return "application/x-shockwave-flash".equals(mimeType);
	}
	
	public static boolean isDocument(String mimeType) {
		return documentMimeTypes.values().contains(mimeType);
	}
	
	public static boolean isZip(String mimeType) {
		return zippableMimeTypes.values().contains(mimeType);
	}
	
	public static boolean isProjectModel(String mimeType) {
		return isZip(mimeType);
	}
	
	public static Resource build(String id, String url, String name, long size, String type,
			String thumbnail, String description, String locale) {
		Resource resource = null;
		
		if(ResourceUtils.isImage(type)) {
			
			resource = new ImageResource(id, url, 
					name, size, type, thumbnail, description, locale, null);
			
		}else if(ResourceUtils.isVideo(type)) {
			
			resource = new VideoResource(id, url, 
					name, size, type, thumbnail, description, locale, null);
			
		}else if(ResourceUtils.isSWF(type)) {

			resource = new SWFResource(id, url, 
					name, size, type, thumbnail, description, locale, null);

		}else if(ResourceUtils.isAudio(type)) {

			resource = new AudioResource(id, url, 
					name, size, type, thumbnail, description, locale, null);
			
		}else if(ResourceUtils.isDocument(type)) {

			resource = new DocumentResource(id, url, 
					name, size, type, thumbnail, description, locale);
			
		}else {
			
			resource = new Resource(id, url, 
				name, size, type, thumbnail,description, locale);
			
		}
		
		return resource;
	}
	
	public static Info build(HasInfo<?> hasInfo) {
		Info info = null;
		if(hasInfo instanceof ImageResource)
			info = new ImageInfo();
		else if(hasInfo instanceof VideoResource)
			info = new VideoInfo();
		else if(hasInfo instanceof SWFResource)
			info = new SWFInfo();
		return info;
	}
}
