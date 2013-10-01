package com.paraschool.editor.client.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paraschool.commons.share.Author;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.Template;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.editor.shared.LocaleDTO;
import com.paraschool.editor.shared.Parameters;
import com.paraschool.editor.shared.ProjectExporterDescriptor;
import com.paraschool.editor.shared.ProjectModel;
import com.paraschool.editor.shared.SampleDetails;
import com.paraschool.editor.shared.exception.MissingUserException;
import com.paraschool.editor.shared.exception.security.ApplicationSecurityException;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
@RemoteServiceRelativePath("app")
public interface EditorService extends RemoteService {
	
	Parameters getParameters();
	
	Author getCurrentAutor();
	
	ArrayList<TemplateDetails> getTemplates() throws MissingUserException, ApplicationSecurityException;
	HashMap<ProjectModel.Owner, ArrayList<ProjectDetails>> getModels() throws MissingUserException, ApplicationSecurityException;
	Template getTemplate(String name) throws MissingUserException, ApplicationSecurityException;
	ArrayList<ProjectExporterDescriptor> getExporters();
	List<LocaleDTO> supportedLocales();
	List<SampleDetails> getSamples(String module, String locale);
	Map<String, Resource> installSampleInProject(ProjectDetails projectDetails, SampleDetails sampleDetails);
	String getSampleContent(SampleDetails sampleDetails);
	boolean deleteModel(String id) throws MissingUserException, ApplicationSecurityException;
}
