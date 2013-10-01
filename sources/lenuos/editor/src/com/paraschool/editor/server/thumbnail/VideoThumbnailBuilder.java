package com.paraschool.editor.server.thumbnail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.paraschool.editor.server.thumbnail.DecodeAndCaptureFrames.ImageDecoded;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;


public class VideoThumbnailBuilder extends ImageThumbnailBuilder implements
		ThumbnailBuilder {

	@Override
	public InputStream make(InputStream media) throws Throwable, NoClassDefFoundError {
		IContainer container = IContainer.make();
		if( container.open(media, null) < 0)
			throw new Exception("Unable to open stream");
		
		File f = File.createTempFile("Thumbnail", "");
		final FileOutputStream out = new FileOutputStream(f);
		
		new DecodeAndCaptureFrames(ToolFactory.makeReader(container),new ImageDecoded() {
			@Override
			public void onDecode(BufferedImage image) {
				try {
					ImageIO.write(image, "png", out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 1);
		
		out.close();
		
		FileInputStream in = new FileInputStream(f);
		InputStream result = super.make(in);
		in.close();
		return result;
	}

	
}
