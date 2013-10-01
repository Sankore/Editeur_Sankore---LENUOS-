package com.paraschool.editor.server.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class XuggleDemo {

	/**
	 * @param args
	 */
	private static Log logger = LogFactory.getLog(XuggleDemo.class);
	
	public static void main(String[] args) {
		IContainer container = IContainer.make();
		if( container.open("/Users/bathily/Desktop/test.mp3", IContainer.Type.READ, null) < 0)
			throw new RuntimeException("");
		
		int numStreams = container.getNumStreams();
		long duration = container.getDuration();
		long size = container.getFileSize();
		
		logger.debug("Durée : "+ duration/Global.DEFAULT_PTS_PER_SECOND +" s");
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
				logger.debug("-- Taux : "+coder.getSampleRate());
				logger.debug("-- Channels : "+coder.getChannels());
				logger.debug("-- Format :"+coder.getSampleFormat());
				
			}
		}
		
		//IMediaReader reader = ToolFactory.makeReader(container);
		/*
		IMediaWriter writer = ToolFactory.makeWriter("/Users/bathily/Desktop/test.mp4", reader);
		reader.addListener(writer);
		while (reader.readPacket() != null) {}
		*/
		//new com.paraschool.editor.server.DecodeAndCaptureFrames("/Users/dbathily/Desktop/test.mov");
		//new com.paraschool.editor.server.thumbnail.DecodeAndCaptureFrames(reader,new File("/Users/bathily/Desktop/"), 1);
	}

}
