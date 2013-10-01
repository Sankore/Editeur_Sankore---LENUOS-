package com.paraschool.editor.client.rpc;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceDetails;
import com.paraschool.editor.client.rpc.ProjectService.Sort;
import com.paraschool.editor.shared.LocaleDTO;
import com.paraschool.editor.shared.Page;
import com.paraschool.editor.shared.ProjectModelRequest;

/*
 * Created at 9 juil. 2010
 * By Didier Bathily
 */
public interface ProjectServiceAsync {

	void create(ProjectDetails details, ProjectModelRequest modelRequest, AsyncCallback<Project> callback);

	void get(ProjectDetails details, AsyncCallback<Project> callback);

	void get(ProjectDetails details, String locale,	AsyncCallback<Project> callback);
	
	void getProjects(int page, Sort sort, AsyncCallback<Page<ProjectDetails>> callback);

	void getRecentProjects(AsyncCallback<List<ProjectDetails>> callback);

	void save(Project project, AsyncCallback<Boolean> callback);
	
	void removeResource(ProjectDetails details, ResourceDetails resourceDetails,
			AsyncCallback<Boolean> callback);

	void preview(Project project, AsyncCallback<Boolean> callback);

	void export(Project project, Map<String, String> options, List<String> exportersId, AsyncCallback<String> callback);

	void publish(Project project, Map<String, String> options, AsyncCallback<String> callback);

	void delete(ProjectDetails details, AsyncCallback<Boolean> callback);

	void addResource(ProjectDetails details, String xml, AsyncCallback<Resource> callback);

	void addResource(ProjectDetails details, Resource resource, AsyncCallback<Resource> callback);

	void createModel(ProjectDetails details, AsyncCallback<Boolean> callback);

	void removeIcon(ProjectDetails details, AsyncCallback<Boolean> callback);

	void create(ProjectDetails details, String locale, AsyncCallback<Project> callback);

	void getLocales(ProjectDetails details,
			AsyncCallback<List<LocaleDTO>> callback);
	
	void deleteLocale(ProjectDetails details, String locale,
			AsyncCallback<Boolean> callback);

	void exportAfterPageSplitted(Project project, Map<String, String> options,
			List<String> exportersId, AsyncCallback<List<String>> callback);

	void clean(ProjectDetails details, AsyncCallback<Boolean> callback);

	void exportPageToProject(Project project, Map<String, String> options,
			List<String> exportersId, int pageIndex,
			AsyncCallback<String> callback);

}
