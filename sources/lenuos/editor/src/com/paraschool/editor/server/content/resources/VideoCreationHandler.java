package com.paraschool.editor.server.content.resources;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paraschool.commons.share.VideoInfo;
import com.paraschool.commons.share.VideoResource;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.thumbnail.ThumbnailBuilder;
import com.paraschool.editor.server.thumbnail.VideoThumbnailBuilder;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class VideoCreationHandler implements ResourceCreationHandler<VideoResource> {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private VideoInfo makeVideoInfo(InputStream resourceStream) {
		IContainer container = IContainer.make();
		if( container.open(resourceStream, null) < 0)
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

				logger.debug("-- Largeur : "+coder.getWidth());
				logger.debug("-- Hauteur : "+coder.getHeight());
				logger.debug("-- Format :"+coder.getPixelType());
			}else if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				logger.debug("-- Taux : "+coder.getSampleRate());
				logger.debug("-- Channels : "+coder.getChannels());
				logger.debug("-- Format :"+coder.getSampleFormat());

			}
		}
		return new VideoInfo(duration, width, height, codec, format);
	}

	@Override
	public void onCreate(VideoResource resource, ProjectManager projectManager) {
		InputStream resourceStream = projectManager.getResource(resource);
		if(resourceStream != null)
			try {
				resource.setInfo(makeVideoInfo(resourceStream));
				ThumbnailBuilder thumbnailBuilder = new VideoThumbnailBuilder();
				InputStream thumbnail = thumbnailBuilder.make(resourceStream);
				projectManager.addThumbnailForResource(resource, thumbnail, thumbnail.available());
				thumbnail.close();
			}catch (NoClassDefFoundError e) {
				logger.error("Xuggler jars was not found in your classpaths. ["+e.getMessage()+"]");
			} catch (Throwable e) {
				e.printStackTrace();
			}finally{
				try {
					resourceStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
	}

}
