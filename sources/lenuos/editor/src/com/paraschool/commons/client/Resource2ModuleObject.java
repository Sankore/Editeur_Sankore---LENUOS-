package com.paraschool.commons.client;

import com.paraschool.commons.share.AudioInfo;
import com.paraschool.commons.share.AudioResource;
import com.paraschool.commons.share.DocumentResource;
import com.paraschool.commons.share.ImageInfo;
import com.paraschool.commons.share.ImageResource;
import com.paraschool.commons.share.LinkResource;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.SWFInfo;
import com.paraschool.commons.share.SWFResource;
import com.paraschool.commons.share.VideoInfo;
import com.paraschool.commons.share.VideoResource;
import com.paraschool.editor.api.client.ModuleObject;

/*
 * Created at 28 août 2010
 * By bathily
 */
public final class Resource2ModuleObject {

	public static ModuleObject toModuleObject(Resource resource) {
		if(resource == null) return null;
		
		ModuleObject object = new ModuleObject(resource.getId(), resource.getName(), resource.getUrl(), resource.getSize(), resource.getMimetype(), ModuleObject.Type.OTHER);
		
		if(resource instanceof ImageResource){
			object.setType(ModuleObject.Type.IMAGE);
			
			ImageInfo info = ((ImageResource)resource).getInfo();
			
			if(info != null){
				object.setWidth(info.getWidth());
				object.setHeight(info.getHeight());
			}
		}
			
		else if(resource instanceof VideoResource){
			object.setType(ModuleObject.Type.VIDEO);
			
			VideoInfo info = ((VideoResource)resource).getInfo();
			
			if(info != null){
				object.setWidth(info.getWidth());
				object.setHeight(info.getHeight());
				object.setDuration(info.getDuration());
				object.setCodec(info.getCodec());
				object.setFormat(info.getFormat());
			}
		}
			
		else if(resource instanceof SWFResource){
			object.setType(ModuleObject.Type.SWF);
			
			SWFInfo info = ((SWFResource)resource).getInfo();
			
			if(info != null){
				object.setWidth(info.getWidth());
				object.setHeight(info.getHeight());
				object.setVersion(info.getVersion());
				object.setFrame(info.getFrame());
				object.setRate(info.getRate());
				object.setAs3(info.getAs3());
				object.setNetwork(info.getNetwork());
				object.setGpu(info.getGpu());
			}
		}
		else if(resource instanceof AudioResource){
			object.setType(ModuleObject.Type.SOUND);
			
			AudioInfo info = ((AudioResource)resource).getInfo();
			
			if(info != null){
				object.setDuration(info.getDuration());
				object.setCodec(info.getCodec());
				object.setFormat(info.getFormat());
			}
		}
		else if(resource instanceof DocumentResource){
			object.setType(ModuleObject.Type.DOCUMENT);
		}
		else if(resource instanceof LinkResource){
			object.setType(ModuleObject.Type.LINK);
		}
		return object;
	}
}
