package com.paraschool.editor.server.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.mortbay.log.Log;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceDetails;
import com.paraschool.editor.server.Marshaller;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.ProjectManagerProvider;
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
@Singleton
@SuppressWarnings("serial")
public class ProjectServiceImpl extends CustomRemoteServiceServlet implements
com.paraschool.editor.client.rpc.ProjectService {

	@Inject	private ProjectManagerProvider projectManagerProvider;
	@Inject private Marshaller marshaller;
	
	@Override
	@RequiresUser
	public Project create(ProjectDetails details, ProjectModelRequest modelRequest) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		return projectManager.createProject(modelRequest);
	}
	
	@RequiresUser
	@Override
	public Project create(ProjectDetails details, String locale) throws ProjectException,
			MissingUserException, ApplicationSecurityException {
		return projectManagerProvider.getProjectManager(details).createLocaleVersion(locale);
	}


	@Override
	@RequiresUser
	public Project get(ProjectDetails details) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		return projectManager.get();
	}
	
	@Override
	@RequiresUser
	public Project get(ProjectDetails details, String locale) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		return projectManager.getVersionForLocale(locale);
	}

	@Override
	@RequiresUser
	public boolean delete(ProjectDetails details) throws ProjectException,
			MissingUserException, ApplicationSecurityException {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		return projectManager.delete();
	}
	
	@Override
	public boolean deleteLocale(ProjectDetails details, String locale)
			throws ProjectException, MissingUserException,
			ApplicationSecurityException {
		return projectManagerProvider.getProjectManager(details).deleteLocale(locale);
	}
	
	@Override
	@RequiresUser
	public boolean save(Project project) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(project.getDetails());
		return projectManager.save(project);
	}
	
	@Override
	@RequiresUser
	public boolean preview(Project project) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(project.getDetails());
		return projectManager.preview(project);
	}
	
	@Override
	@RequiresUser
	public Page<ProjectDetails> getProjects(int page, Sort sort) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
		return projectManager.getProjects(page, 4, sort);
	}

	@Override
	@RequiresUser
	public List<ProjectDetails> getRecentProjects() {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
		return projectManager.getProjects(0, 4, Sort.DATE).getData();
	}

	@Override
	@RequiresUser
	public boolean createModel(ProjectDetails details) throws ProjectException,	MissingUserException, ApplicationSecurityException {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		return projectManager.createModel() != null;
	}

	@Override
	@RequiresUser
	public boolean removeResource(ProjectDetails details, ResourceDetails resourceDetails) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		return projectManager.removeResource(resourceDetails);
	}
	
	@RequiresUser
	@Override
	public String export(Project project, Map<String, String> options, List<String> exportersId) throws MissingUserException, ApplicationSecurityException, MissingFeatureException {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(project.getDetails());
		return projectManager.export(project, options, exportersId);
	}
	
	@RequiresUser
	@Override
	public String exportPageToProject(Project project,
			Map<String, String> options, List<String> exportersId, int pageIndex)
			throws MissingUserException, ApplicationSecurityException,
			MissingFeatureException {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(project.getDetails());
		List<String> urls = projectManager.exportPages(project, new Integer[]{pageIndex}, options, exportersId);
		if(urls != null && urls.size() > 0) return urls.get(0);
		return null;
	}

	@RequiresUser
	@Override
	public List<String> exportAfterPageSplitted(Project project,
			Map<String, String> options, List<String> exportersId)
			throws MissingUserException, ApplicationSecurityException,
			MissingFeatureException {
		
		ProjectManager projectManager = projectManagerProvider.getProjectManager(project.getDetails());
		Integer[] indexes = new Integer[project.getPages().size()];
		for(int i = 0 ; i < project.getPages().size() ; i++) {
			indexes[i] = i;
		}
		
		return projectManager.exportPages(project, indexes, options, exportersId);
	}

	@RequiresUser
	@Override
	public String publish(Project project, Map<String, String> options) throws MissingUserException, ApplicationSecurityException, MissingFeatureException {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(project.getDetails());
		String url = projectManager.publish(project, options, getThreadLocalRequest());
		logger.debug("Project exported and available at ["+url+"]");
		return url;
	}
	
	@Override
	public Resource addResource(ProjectDetails details, String xml) {
		try{
			return addResource(details, (Resource)marshaller.fromXML(xml));
		}catch (Exception e) {
			Log.warn(e.getCause());
		}
		return null;
	}

	@Override
	public Resource addResource(ProjectDetails details, Resource resource) {
		if(resource == null)
			return null;
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		return projectManager.addResource(resource);
	}

	@Override
	public boolean removeIcon(ProjectDetails details) throws MissingUserException, ApplicationSecurityException {
		return projectManagerProvider.getProjectManager(details).removeIcon();
	}

	@Override
	public List<LocaleDTO> getLocales(ProjectDetails details)
			throws ProjectException, MissingUserException,
			ApplicationSecurityException {

		List<String> locales = projectManagerProvider.getProjectManager(details).getLocales();
		List<LocaleDTO> localesDTO = new ArrayList<LocaleDTO>(locales.size());
		
		for(String l : locales) {
			localesDTO.add(new LocaleDTO(l, LocaleUtils.toLocale(l).getDisplayName()));
		}
		
		Collections.sort(localesDTO, Ordering.natural().onResultOf(new Function<LocaleDTO, String>() {
			@Override
			public String apply(LocaleDTO input) {
				return input.getDisplayName();
			}
		}));
		
		return localesDTO;
	}

	@Override
	public boolean clean(ProjectDetails details) {
		return projectManagerProvider.getProjectManager(details).clean();
	}
}
