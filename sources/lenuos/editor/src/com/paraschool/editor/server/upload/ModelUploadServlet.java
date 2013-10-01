package com.paraschool.editor.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.ResourceUtils;
import com.paraschool.editor.server.Marshaller;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.ProjectManagerProvider;
import com.paraschool.editor.server.utils.Zip;
import com.paraschool.editor.shared.ProjectModel;

/*
 * Created at 16 déc. 2010
 * By bathily
 */
@SuppressWarnings("serial")
@Singleton
public class ModelUploadServlet extends UploadServlet {

	@Inject	private ProjectManagerProvider projectManagerProvider;
	@Inject private Marshaller marshaller;
	
	@Inject
	public ModelUploadServlet(@Named("model.upload.max.size") long sizeLimit,@Named("upload.sleep") int sleep) {
		super(sizeLimit, sleep);
	}

	@Override
	protected String executeAction(HttpServletRequest request,
			List<FileItem> items) {
		
		String response = "<response status=\"ok\">";
		
		ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
		File directory = new File(projectManager.getApplicationManager().getUserProjectModelsDirectory());
		if(!directory.exists()) directory.mkdir();
		for (FileItem item : items) {
			logger.debug(item.getContentType());
			//TODO Found a way to add application/widget as content type of .wgt in apache commons fileupload
			if (false == item.isFormField() && (ResourceUtils.isProjectModel(item.getContentType()) || item.getName().endsWith(".wgt"))) {
				try {
					/// Create a new file.
					String itemName = item.getName();
					String suffix = "";
					
					/// Get the file extension
					int extIndex = itemName.lastIndexOf(".");
					if(extIndex > 1)
						suffix = itemName.substring(extIndex, itemName.length());
					
					//FIXME USE PROJECTMANAGER FOR THIS
					
					File file = File.createTempFile("PSEditorModel-", suffix, directory);
					String id = file.getName();
					item.write(file);
					
					logger.debug("Model uploaded file write in ["+id+"]");

					File tmp = new File(System.getProperty("java.io.tmpdir"),id);
					tmp.mkdir();
					InputStream input = new FileInputStream(file);
					Zip.unzip(input, tmp);
					logger.debug("Model unzipped at ["+tmp.getAbsolutePath()+"]");
					
					try{
						input.close();
					}catch (IOException e) {logger.error(e);}
					
					try{
						InputStream project = new FileInputStream(new File(tmp,"project.xml"));
						Project importedProject = (Project) marshaller.fromXML(project);
						try{
							project.close();
						}catch (IOException e) {logger.error(e);}
						
						ProjectDetails details = importedProject.getDetails();
						details.setId(id);
						details.setDate(new Date());
						ProjectModel model = new ProjectModel(details, id);
						OutputStream output = new FileOutputStream(new File(directory, id+".xml"));
						marshaller.toXML(model, output);
						try{
							output.close();
						}catch (IOException e) {logger.error(e);}
						
						response += marshaller.toXML(model);
					}catch (Exception e) {
						logger.warn(e);
						e.printStackTrace();
					}
					
					logger.debug("Model ["+id+"] saved at ["+file.getAbsolutePath()+"]");

				} catch (Exception e) {
					logger.warn(e);
					e.printStackTrace();
				}
			}
		}
		return response+"</response>";
	}

	
}
