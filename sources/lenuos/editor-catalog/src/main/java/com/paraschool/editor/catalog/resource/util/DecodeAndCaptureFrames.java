package com.paraschool.editor.catalog.resource.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

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

	private List<File> files;
	private File destinationDirectory;

	/** Construct a DecodeAndCaptureFrames which reads and captures
	 * frames from a video file.
	 * 
	 * @param filename the name of the media file to read
	 */
	public DecodeAndCaptureFrames(String filename, File destinationDirectory, int maxCount)
	{
		// create a media reader for processing video
		this(ToolFactory.makeReader(filename),destinationDirectory, maxCount);
	}
	
	public DecodeAndCaptureFrames(String filename, File destinationDirectory){
		this(filename, destinationDirectory, -1);
	}
	
	public DecodeAndCaptureFrames(IMediaReader reader, File destinationDirectory, int maxCount) {
		this(reader, destinationDirectory, DEFAULT_SECONDS_BETWEEN_FRAMES, maxCount);
	}
	
	public DecodeAndCaptureFrames(IMediaReader reader, File destinationDirectory) {
		this(reader, destinationDirectory, DEFAULT_SECONDS_BETWEEN_FRAMES, -1);
	}
	
	public DecodeAndCaptureFrames(IMediaReader reader, File destinationDirectory, double interval, int maxCount) {

		this.destinationDirectory = destinationDirectory;
		this.interval =	(long)(Global.DEFAULT_PTS_PER_SECOND * interval);
		this.files = new ArrayList<File>();
		
		// stipulate that we want BufferedImages created in BGR 24bit color space		
		reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		reader.addListener(this);
		
		while (reader.readPacket() == null && (maxCount == -1 || files.size() < maxCount))
			do {} while(false);
		reader.close();
	}
	
	public List<File> getFiles() {
		return files;
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
				// Make a temporary file name

				File file = File.createTempFile("frame-", ".png", destinationDirectory);

				// write out PNG
				ImageIO.write(event.getImage(), "png", file);
				files.add(file);
				
				// indicate file written

				if(logger.isDebugEnabled()){
					double seconds = ((double)event.getTimeStamp()) / Global.DEFAULT_PTS_PER_SECOND;				
					logger.debug(String.format("at elapsed time of %6.3f seconds wrote: %s", seconds, file));
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
