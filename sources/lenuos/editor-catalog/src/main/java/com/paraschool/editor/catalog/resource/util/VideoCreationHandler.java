package com.paraschool.editor.catalog.resource.util;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paraschool.editor.catalog.models.Resource;
import com.paraschool.editor.catalog.models.TechnicalData;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class VideoCreationHandler implements ResourceCreationHandler {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private final String fileSeparator = System.getProperty("file.separator");
	private final String rootname;
	private final String pathname = ResourceUtil.getPath(Calendar.getInstance());
	
	public VideoCreationHandler(String rootname) {
		super();
		this.rootname = rootname;
	}

	private Set<TechnicalData> makeVideoInfo(String path) {
		
		Set<TechnicalData> technicalDataSet = new HashSet<TechnicalData>();
		TechnicalData technicalData = null;
		
		IContainer container = IContainer.make();
		if( container.open(path, IContainer.Type.READ, null) < 0)
			return null;

		int numStreams = container.getNumStreams();
		long duration = container.getDuration() /Global.DEFAULT_PTS_PER_SECOND ;
		long size = container.getFileSize();
		int width = 0, height = 0;
		String codec = null, format = null;

		logger.debug("Durée : "+ duration +" s");
		logger.debug("Taille : "+size+" octets");
		logger.debug("Nombre de pistes : "+numStreams);

		for(int i=0 ; i < numStreams ; i++) {
			logger.debug("Piste "+i);
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			logger.debug("-- Langue : "+stream.getLanguage());
			logger.debug("-- Type : "+coder.getCodecType());
			logger.debug("-- Codec : "+coder.getCodecID());
			if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				codec = coder.getCodecID().name();
				format = coder.getPixelType().name();
				width = coder.getWidth();
				height = coder.getHeight();
				
				technicalData = new TechnicalData("codec", codec);
				technicalDataSet.add(technicalData);
				technicalData = new TechnicalData("format", format);
				technicalDataSet.add(technicalData);
				technicalData = new TechnicalData("width", new Integer(width).toString());
				technicalDataSet.add(technicalData);
				technicalData = new TechnicalData("height",  new Integer(height).toString());
				technicalDataSet.add(technicalData);
				
				logger.debug("-- Largeur : "+coder.getWidth());
				logger.debug("-- Hauteur : "+coder.getHeight());
				logger.debug("-- Format :"+coder.getPixelType());
			}else if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				logger.debug("-- Taux : "+coder.getSampleRate());
				logger.debug("-- Channels : "+coder.getChannels());
				logger.debug("-- Format :"+coder.getSampleFormat());

			}
		}
		return technicalDataSet;
	}

	public void onCreate(Resource resource) {
		
		File dir = new File(rootname + pathname);
		String source = rootname + resource.getFileUrl();
		File thumbnail = null;
		try {
			ThumbnailBuilder thumbnailBuilder = new VideoThumbnailBuilder();
			thumbnail = thumbnailBuilder.make(source, dir);
			if (thumbnail != null){			
				resource.setThumbnailUrl(pathname + fileSeparator + thumbnail.getName());
			}
			resource.setTechnicalDatas(makeVideoInfo(source));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}


}
