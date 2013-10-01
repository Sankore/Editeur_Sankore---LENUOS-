package com.paraschool.editor.server.content.resources;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paraschool.commons.share.AudioInfo;
import com.paraschool.commons.share.AudioResource;
import com.paraschool.editor.server.ProjectManager;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class AudioCreationHandler implements ResourceCreationHandler<AudioResource> {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void onCreate(final AudioResource resource, final ProjectManager projectManager) {
		InputStream resourceStream = projectManager.getResource(resource);
		if(resourceStream != null)
			try {
				resource.setInfo(makeAudioInfo(resourceStream));
			}catch (NoClassDefFoundError e) {
				logger.error("Xuggler jars was not found in your classpaths. ["+e.getMessage()+"]");
			} catch (Throwable e) {
				e.printStackTrace();
			}finally {
				try {
					resourceStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	private AudioInfo makeAudioInfo(InputStream resourceStream) {
		IContainer container = IContainer.make();
		if( container.open(resourceStream, null) < 0)
			return null;

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

				logger.debug("-- Taux : "+coder.getSampleRate());
				logger.debug("-- Channels : "+coder.getChannels());
				logger.debug("-- Format :"+coder.getSampleFormat());

			}
		}
		return new AudioInfo(duration, codec, format);
	}	
	
}
