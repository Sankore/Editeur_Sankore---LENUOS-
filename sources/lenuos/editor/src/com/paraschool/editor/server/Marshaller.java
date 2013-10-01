package com.paraschool.editor.server;

import com.paraschool.commons.share.AudioResource;
import com.paraschool.commons.share.DocumentResource;
import com.paraschool.commons.share.ImageResource;
import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.LinkResource;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.SWFResource;
import com.paraschool.commons.share.Template;
import com.paraschool.commons.share.VideoResource;
import com.paraschool.editor.server.security.XWikiAuthResponse;
import com.paraschool.editor.shared.ProjectModel;
import com.paraschool.editor.shared.Sample;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */

public class Marshaller extends XStream {

	public Marshaller() {
		super();
		init();
	}
	
	public Marshaller(HierarchicalStreamDriver driver) {
		super(driver);
		init();
	}
	
	private void init() {
		processAnnotations(new Class[] {Project.class, Template.class, ProjectModel.class, Sample.class, Interactivity.class, 
				Resource.class, ImageResource.class, VideoResource.class,
				SWFResource.class, AudioResource.class, DocumentResource.class, LinkResource.class,
				PublicationTicket.class,
				XWikiAuthResponse.class} );
	}
}
