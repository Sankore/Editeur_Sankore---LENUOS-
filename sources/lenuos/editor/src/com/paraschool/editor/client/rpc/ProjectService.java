package com.paraschool.editor.client.rpc;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceDetails;
import com.paraschool.editor.shared.LocaleDTO;
import com.paraschool.editor.shared.Page;
import com.paraschool.editor.shared.ProjectModelRequest;
import com.paraschool.editor.shared.exception.MissingFeatureException;
import com.paraschool.editor.shared.exception.MissingUserException;
import com.paraschool.editor.shared.exception.ProjectException;
import com.paraschool.editor.shared.exception.security.ApplicationSecurityException;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
@RemoteServiceRelativePath("project")
public interface ProjectService extends RemoteService {

	public enum Sort {
		NONE,
		DATE,
		NAME
	}
	
	Project create(ProjectDetails details, ProjectModelRequest modelRequest) throws ProjectException, MissingUserException, ApplicationSecurityException;
	Project get(ProjectDetails details) throws ProjectException, MissingUserException, ApplicationSecurityException;
	Project get(ProjectDetails details, String locale) throws ProjectException, MissingUserException, ApplicationSecurityException;
	boolean delete(ProjectDetails details) throws ProjectException, MissingUserException, ApplicationSecurityException;
	boolean deleteLocale(ProjectDetails details, String locale) throws ProjectException, MissingUserException, ApplicationSecurityException;
	
	boolean save(Project project) throws MissingUserException, ApplicationSecurityException;
	boolean preview(Project project) throws MissingUserException, ApplicationSecurityException;
	
	boolean clean(ProjectDetails details);
	
	Resource addResource(ProjectDetails details, String xml);
	Resource addResource(ProjectDetails details, Resource resource);
	boolean removeResource(ProjectDetails details, ResourceDetails resourceDetails) throws MissingUserException, ApplicationSecurityException;
	boolean removeIcon(ProjectDetails details) throws MissingUserException, ApplicationSecurityException;
	
	Page<ProjectDetails> getProjects(int page, Sort sort) throws MissingUserException, ApplicationSecurityException;
	List<ProjectDetails> getRecentProjects() throws MissingUserException, ApplicationSecurityException;
	
	String export(Project project, Map<String, String> options, List<String> exportersId) throws MissingUserException, ApplicationSecurityException, MissingFeatureException;
	String publish(Project project, Map<String, String> options) throws MissingUserException, ApplicationSecurityException, MissingFeatureException;
	
	String exportPageToProject(Project project, Map<String, String> options, List<String> exportersId, int pageIndex) throws MissingUserException, ApplicationSecurityException, MissingFeatureException;
	List<String> exportAfterPageSplitted(Project project, Map<String, String> options, List<String> exportersId) throws MissingUserException, ApplicationSecurityException, MissingFeatureException;
	
	boolean createModel(ProjectDetails details) throws ProjectException, MissingUserException, ApplicationSecurityException;
	
	Project create(ProjectDetails details, String locale) throws ProjectException, MissingUserException, ApplicationSecurityException;
	List<LocaleDTO> getLocales(ProjectDetails details) throws ProjectException, MissingUserException, ApplicationSecurityException;
}
