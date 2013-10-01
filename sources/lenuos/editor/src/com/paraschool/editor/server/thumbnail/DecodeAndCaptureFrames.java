package com.paraschool.editor.server.thumbnail;

import java.awt.image.BufferedImage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

/**
 * Using {@link IMediaReader}, takes a media container, finds the first video stream, decodes that
 * stream, and then writes video frames out to a PNG image file every x
 * seconds, based on the video presentation timestamps.
 *
 * @author dbathily
 */

public class DecodeAndCaptureFrames extends MediaListenerAdapter
{
	interface ImageDecoded {
		void onDecode(BufferedImage image);
	}
	
	private static Log logger = LogFactory.getLog(DecodeAndCaptureFrames.class);
	
	/** 
	 * The number of seconds between frames.
	 */
	private final long interval;
	public static final double DEFAULT_SECONDS_BETWEEN_FRAMES = 5;

	/** Time of last frame write. */
	private static long mLastPtsWrite = Global.NO_PTS;

	/**
	 * The video stream index, used to ensure we display frames from one
	 * and only one video stream from the media container.
	 */
	private int mVideoStreamIndex = -1;
	private final ImageDecoded callback;
	private int count = 0;
	
	public DecodeAndCaptureFrames(String filename, ImageDecoded callback){
		this(filename, callback, -1);
	}
	
	public DecodeAndCaptureFrames(String filename, ImageDecoded callback, int maxCount)
	{
		this(ToolFactory.makeReader(filename), callback, maxCount);
	}
	
	public DecodeAndCaptureFrames(IMediaReader reader, ImageDecoded callback) {
		this(reader, callback, DEFAULT_SECONDS_BETWEEN_FRAMES, -1);
	}

	public DecodeAndCaptureFrames(IMediaReader reader, ImageDecoded callback, int maxCount) {
		this(reader, callback, DEFAULT_SECONDS_BETWEEN_FRAMES, maxCount);
	}
		
	public DecodeAndCaptureFrames(IMediaReader reader, ImageDecoded callback, double interval, int maxCount) {
		this.callback = callback;
		this.interval =	(long)(Global.DEFAULT_PTS_PER_SECOND * interval);
		
		// stipulate that we want BufferedImages created in BGR 24bit color space		
		reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		reader.addListener(this);
		
		while (reader.readPacket() == null && (maxCount == -1 || count < maxCount))
			do {} while(false);
		reader.close();
	}

	/** 
	 * Called after a video frame has been decoded from a media stream.
	 * Optionally a BufferedImage version of the frame may be passed
	 * if the calling {@link IMediaReader} instance was configured to
	 * create BufferedImages.
	 * 
	 * This method blocks, so return quickly.
	 */

	public void onVideoPicture(IVideoPictureEvent event)
	{
		try
		{
			// if the stream index does not match the selected stream index,
			// then have a closer look

			if (event.getStreamIndex() != mVideoStreamIndex)
			{
				// if the selected video stream id is not yet set, go ahead an
				// select this lucky video stream

				if (-1 == mVideoStreamIndex)
					mVideoStreamIndex = event.getStreamIndex();

				// otherwise return, no need to show frames from this video stream

				else
					return;
			}

			// if uninitialized, backdate mLastPtsWrite so we get the very
			// first frame

			if (mLastPtsWrite == Global.NO_PTS)
				mLastPtsWrite = event.getTimeStamp() - interval;

			// if it's time to write the next frame

			if (event.getTimeStamp() - mLastPtsWrite >= interval)
			{
								
				if(callback != null)
					callback.onDecode(event.getImage());
				count++;
				// indicate file written

				if(logger.isDebugEnabled()){
					double seconds = ((double)event.getTimeStamp()) / Global.DEFAULT_PTS_PER_SECOND;				
					logger.debug(String.format("at elapsed time of %6.3f seconds wrote", seconds));
				}
				
				// update last write time
				mLastPtsWrite += interval;
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
