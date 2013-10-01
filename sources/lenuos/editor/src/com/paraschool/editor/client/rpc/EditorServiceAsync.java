package com.paraschool.editor.client.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public interface EditorServiceAsync {

	void getTemplate(String name, AsyncCallback<Template> callback);

	void getTemplates(AsyncCallback<ArrayList<TemplateDetails>> callback);

	void getExporters(
			AsyncCallback<ArrayList<ProjectExporterDescriptor>> callback);

	void getModels(AsyncCallback<HashMap<ProjectModel.Owner, ArrayList<ProjectDetails>>> callback);

	void deleteModel(String id, AsyncCallback<Boolean> callback);

	void getParameters(AsyncCallback<Parameters> callback);

	void getCurrentAutor(AsyncCallback<Author> callback);

	void supportedLocales(AsyncCallback<List<LocaleDTO>> callback);

	void getSamples(String module, String locale,
			AsyncCallback<List<SampleDetails>> callback);

	void installSampleInProject(ProjectDetails projectDetails,
			SampleDetails sampleDetails, AsyncCallback<Map<String, Resource>> callback);

	void getSampleContent(SampleDetails sampleDetails,
			AsyncCallback<String> callback);

}
