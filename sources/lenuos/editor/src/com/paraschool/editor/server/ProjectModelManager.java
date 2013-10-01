package com.paraschool.editor.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.editor.server.assets.FileRepository;
import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.server.content.models.ProjectModelProviderFactory;
import com.paraschool.editor.server.utils.Zip;
import com.paraschool.editor.shared.ProjectModel;
import com.paraschool.editor.shared.ProjectModel.Owner;
import com.paraschool.editor.shared.ProjectModelRequest;

/*
 * Created at 16 nov. 2010
 * By bathily
 */
public class ProjectModelManager {

	private static final Log logger = LogFactory.getLog(ProjectModelManager.class);

	@Inject	private ProjectManagerProvider projectManagerProvider;
	@Inject private Set<ContentProvider<ProjectModel>> modelProviders;
	@Inject @Named("user.models") private ProjectModelProviderFactory userModelProviderFactory;
	@Inject private FileRepository fileRepository;

	@Inject
	private ProjectModelManager(){}
	
	public ProjectModel getModel(ProjectModelRequest modelRequest) {
		String id = modelRequest.getId();
		if(id == null) return null;
		
		ProjectModel model = null;
		
		if(modelRequest.getOwner().equals(ProjectModel.Owner.USER)) {
			ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
			
			// Check from user's project models
			ContentProvider<ProjectModel> provider = userModelProviderFactory.get(projectManager.getApplicationManager().getUserProjectModelsDirectory());
			provider.list();
			
			if( (model = provider.get(id)) != null) return model;
			
		}else{
			for(ContentProvider<ProjectModel> modelProvider : modelProviders) {
				if((model = modelProvider.get(id)) != null) return model;
			}
		}
		
		return null;
	}
	
	public void installModel(ProjectModel model, ProjectModel.Owner owner,  String directory) throws Exception {
		InputStream zipInputstream = null;
		if(owner.equals(Owner.USER)){
			if(!fileRepository.exist(model.getLocation(),"")){
				logger.warn("Failed to find model ["+model.getId()+"] file at ["+model.getLocation()+"] for user");
				throw new Exception();	
			}
			zipInputstream = fileRepository.getAsset(model.getLocation()).getInputStream();
		}else{
			File zip = new File(model.getLocation());
			if(zip == null || !zip.exists()) {
				logger.warn("Failed to find model ["+model.getId()+"] file at ["+model.getLocation()+"]");
				throw new Exception();
			}
			zipInputstream = new FileInputStream(zip);
		}
		logger.debug("Find model package at ["+model.getLocation()+"]. Try to unzip it");
		File tempDir = new File(System.getProperty("java.io.tmp.dir"), UUID.randomUUID().toString());
		Zip.unzip(zipInputstream, tempDir);
		fileRepository.copy(tempDir.getAbsolutePath(), directory, null);
		IOUtils.closeQuietly(zipInputstream);
		
	}
	
	public boolean deleteModel(ContentProvider<ProjectModel> provider, String id) {
		ProjectModel model = provider.get(id);
		if(model == null)
			return false;
		fileRepository.delete(model.getLocation(),"");
		return provider.delete(model.getId());
	}
	
}
