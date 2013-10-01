package com.paraschool.editor.server.content.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieAttributes;
import com.flagstone.transform.MovieHeader;
import com.flagstone.transform.MovieTag;
import com.flagstone.transform.datatype.Bounds;
import com.paraschool.commons.share.SWFInfo;
import com.paraschool.commons.share.SWFResource;
import com.paraschool.editor.server.ProjectManager;

public class SWFCreationHandler implements ResourceCreationHandler<SWFResource> {

	private SWFInfo makeSWFInfo(InputStream resourceStream) {
		Movie movie = new Movie();
		try {
			movie.decodeFromStream(resourceStream);
			MovieHeader header = null;
			MovieAttributes attributes = null;

			for(MovieTag tag : movie.getObjects()) {
				if(header == null && tag instanceof MovieHeader)
					header = (MovieHeader)tag;
				else if(attributes == null && tag instanceof MovieAttributes)
					attributes = (MovieAttributes)tag;

				if(header != null && attributes != null)
					break;
			}

			SWFInfo info = new SWFInfo();

			if(header != null){
				info.setVersion(header.getVersion());
				Bounds b = header.getFrameSize();
				info.setWidth(b.getWidth()/20); //Size is in twip
				info.setHeight(b.getHeight()/20);
				info.setRate(header.getFrameRate());
				info.setFrame(header.getFrameCount());
			}

			if(attributes != null) {
				info.setAs3(attributes.hasAS3());
				info.setGpu(attributes.useGPU());
				info.setNetwork(attributes.useNetwork());
			}

			return info;
		} catch (DataFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onCreate(SWFResource resource, ProjectManager projectManager) {
		InputStream resourceStream = projectManager.getResource(resource);
		if(resourceStream != null){
			resource.setInfo(makeSWFInfo(resourceStream));
			try {
				resourceStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



}
