package com.paraschool.editor.catalog.resource.util;

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

public class AudioCreationHandler implements ResourceCreationHandler {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private final String rootname;
	
	public AudioCreationHandler(String rootname) {
		this.rootname = rootname;
	}

	public void onCreate(Resource resource) {
		
		String source = rootname + resource.getFileUrl();
		try {
			resource.setTechnicalDatas(makeAudioInfo(source));
		}catch (NoClassDefFoundError e) {
			logger.error("Xuggler jars was not found in your classpaths. ["+e.getMessage()+"]");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private Set<TechnicalData> makeAudioInfo(String path) {
		
		Set<TechnicalData> technicalDataSet = new HashSet<TechnicalData>();
		TechnicalData technicalData = null;
		
		IContainer container = IContainer.make();
		if( container.open(path, IContainer.Type.READ, null) < 0)
			 throw new IllegalArgumentException("could not open file: " + path);

		int numStreams = container.getNumStreams();
		long duration = container.getDuration() /Global.DEFAULT_PTS_PER_SECOND ;
		long size = container.getFileSize();

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
				logger.debug("-- Largeur : "+coder.getWidth());
				logger.debug("-- Hauteur : "+coder.getHeight());
				logger.debug("-- Format :"+coder.getPixelType());
			}else if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				codec = coder.getCodecID().name();
				format = coder.getSampleFormat().name();
				
				technicalData = new TechnicalData("codec", codec);
				technicalDataSet.add(technicalData);
				technicalData = new TechnicalData("format", format);
				technicalDataSet.add(technicalData);
				

				logger.debug("-- Taux : "+coder.getSampleRate());
				logger.debug("-- Channels : "+coder.getChannels());
				logger.debug("-- Format :"+coder.getSampleFormat());

			}
		}
		//return new AudioInfo(duration, codec, format);
		return technicalDataSet;
	}	
	
}
