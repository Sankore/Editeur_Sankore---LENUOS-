package com.paraschool.editor.server;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceDetails;
import com.paraschool.editor.client.rpc.ProjectService.Sort;
import com.paraschool.editor.server.assets.FileStream;
import com.paraschool.editor.shared.Page;
import com.paraschool.editor.shared.ProjectModel;
import com.paraschool.editor.shared.ProjectModelRequest;
import com.paraschool.editor.shared.exception.MissingUserException;
import com.paraschool.editor.shared.exception.ProjectException;

public interface ProjectManager {
	
	static final String PROJECT_RESOURCES_THUMBNAILS_DIR = "thumbnails";
	static final String PROJECT_RESOURCES_DIR = "resources";
	static final String PROJECT_FILE_EXT = ".xml";
	static final String PROJECT_FILE_PREFIX = "project";
	static final String PROJECT_FILE= PROJECT_FILE_PREFIX+PROJECT_FILE_EXT;
	
	public abstract ProjectDetails getProjectDetails();

	public abstract InputStream getResource(Resource resource);
	
	public abstract InputStream getStreamForPathInProject(String path);
	
	public abstract FileStream getInProjectResource(String path);
	
	public abstract FileStream getExportResource(String path);
	
	public abstract FileStream getPublicationResource(String path);
	
	public abstract void addThumbnailForResource(Resource resource, InputStream thumbnailStream, long lenght);
	
	public abstract ApplicationManager getApplicationManager();

	public abstract Project createProject(ProjectModelRequest modelRequest);

	public abstract Project createLocaleVersion(String locale);

	public abstract Project get();

	public abstract Project copy(String newName);
	
	public abstract Project getVersionForLocale(String locale);

	public abstract Page<ProjectDetails> getProjects(int page, int limit, Sort sort);

	public abstract boolean delete() throws ProjectException,
			MissingUserException;

	public abstract boolean deleteLocale(String locale)
			throws ProjectException, MissingUserException;

	public abstract boolean save(Project project);

	public abstract boolean preview(Project project);

	public abstract ProjectModel createModel() throws ProjectException;

	public abstract boolean removeResource(ResourceDetails resourceDetails);

	public abstract boolean removeIcon();

	public abstract Resource addResource(Resource resource);

	public abstract String export(Project project, Map<String, String> options, List<String> exportersId);

	public abstract List<String> exportPages(Project project, Integer[] index, Map<String, String> options, List<String> exportersId);
	
	public abstract String export(Project project, File source, Map<String, String> options, List<String> exportersId);
	
	public abstract String publish(Project project, Map<String, String> options, HttpServletRequest request);

	public abstract List<String> getLocales();
	
	public abstract void addResourceFile(File file);

	public abstract String processUploadedResourceFileForProject(List<FileItem> items);

	public abstract String processUploadedIconFileForProject(List<FileItem> items);
	
	public abstract String importExport(FileStream resource);

	public abstract boolean clean();

	

}