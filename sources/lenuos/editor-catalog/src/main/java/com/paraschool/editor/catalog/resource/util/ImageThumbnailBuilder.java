package com.paraschool.editor.catalog.resource.util;

import java.io.File;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

public class ImageThumbnailBuilder implements ThumbnailBuilder {

	public File make(String mediaURL, File destinationDirectory) throws Throwable {
		File media = new File(mediaURL);
		ConvertCmd cmd = new ConvertCmd();
		
		IMOperation op = new IMOperation();
		op.addImage(media.getAbsolutePath());
		
		op.thumbnail(84,84,"^").gravity("center").extent(84, 84);
		
		IMOperation corner = new IMOperation();
		corner.p_clone().alpha("extract").draw("fill black polygon 0,0 0,6 6,0 fill white circle 6,6 6,0");
		corner.addSubOperation(new IMOperation().p_clone().flip()).compose("Multiply").composite();
		corner.addSubOperation(new IMOperation().p_clone().flop()).compose("Multiply").composite();
		op.addSubOperation(corner);
		op.alpha("off").compose("CopyOpacity").composite();
		
		File result = new File(destinationDirectory, media.getName()+".png");
		op.addImage(result.getAbsolutePath());
		
		cmd.run(op);
		return result;
	}

}
