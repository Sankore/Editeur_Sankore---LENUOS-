package com.paraschool.editor.server.rpc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Author;
import com.paraschool.commons.share.IsFile;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.Template;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.editor.client.rpc.EditorService;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.ProjectManagerProvider;
import com.paraschool.editor.server.ProjectModelManager;
import com.paraschool.editor.server.User;
import com.paraschool.editor.server.UserProvider;
import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.server.content.models.ProjectModelProviderFactory;
import com.paraschool.editor.server.exporter.PackageProjectExporter;
import com.paraschool.editor.server.exporter.ProjectExporter;
import com.paraschool.editor.shared.LocaleDTO;
import com.paraschool.editor.shared.Parameters;
import com.paraschool.editor.shared.ProjectExporterDescriptor;
import com.paraschool.editor.shared.ProjectModel;
import com.paraschool.editor.shared.Sample;
import com.paraschool.editor.shared.SampleDetails;
import com.paraschool.editor.shared.exception.MissingUserException;
import com.paraschool.editor.shared.exception.security.ApplicationSecurityException;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
@Singleton
@SuppressWarnings("serial")
public class EditorServiceImpl extends CustomRemoteServiceServlet implements
		EditorService {

	@Inject private Parameters parameters;
	@Inject @Named("security.required") private Boolean securityEnabled;
	@Inject private UserProvider userProvider;
	@Inject private Set<ContentProvider<Template>> templateProviders;
	@Inject private Set<ContentProvider<ProjectModel>> modelProviders;
	
	@Inject @Named("user.models") private ProjectModelProviderFactory userModelProviderFactory;
	@Inject private Set<PackageProjectExporter> exporters;
	@Inject	private ProjectManagerProvider projectManagerProvider;
	@Inject private ProjectModelManager modelManager;
	
	@Inject @Named("default.author.email") private String authorEmail;
	@Inject @Named("default.author.company") private String authorCompany;
	@Inject @Named("default.author.url") private String authorUrl;
	
	private final Map<String, Map<Locale, List<Sample>>> samples = new HashMap<String, Map<Locale,List<Sample>>>();
	
	private final List<LocaleDTO> locales = new ArrayList<LocaleDTO>();
	
	@Inject
	public EditorServiceImpl(Set<ContentProvider<Sample>> sampleProviders) {
		super();
		//this.sampleProviders = sampleProviders;
		for(ContentProvider<Sample> provider : sampleProviders)
			initializeSample(provider);
		
		for(Locale l : Locale.getAvailableLocales()) {
			locales.add(new LocaleDTO(l.toString(), l.getDisplayName()));
		}
		
		Collections.sort(locales, Ordering.natural().onResultOf(new Function<LocaleDTO, String>() {
			@Override
			public String apply(LocaleDTO input) {
				return input.getDisplayName();
			}
		}));
	}
	
	private Locale determineLocaleKey(String toTest) {
		Locale locale = Locale.ROOT;
		if(toTest != null && !toTest.trim().isEmpty())
			try{
				locale = LocaleUtils.toLocale(toTest);
			}catch (IllegalArgumentException e) {}
		return locale;
	}
	
	private void initializeSample(ContentProvider<Sample> provider) {
		for(Sample sample : provider.list()) {
			SampleDetails details = sample.getDetails();
			Map<Locale, List<Sample>> subSamplesMap = samples.get(details.getModule());
			if(subSamplesMap == null) {
				subSamplesMap = new HashMap<Locale, List<Sample>>();
				samples.put(details.getModule(), subSamplesMap);
			}
			
			Locale locale = determineLocaleKey(details.getLocale());
			
			List<Sample> subSamples = subSamplesMap.get(locale);
			if(subSamples == null) {
				subSamples = new ArrayList<Sample>();
				subSamplesMap.put( locale, subSamples);
			}
			subSamples.add(sample);
		}
		if(logger.isDebugEnabled()) {
			for(Entry<String, Map<Locale, List<Sample>>> modules : samples.entrySet()) {
				for(Entry<Locale, List<Sample>> locales : modules.getValue().entrySet()) {
					for(Sample sample : locales.getValue()) {
						logger.debug("Module:"+modules.getKey()+", locale:"+locales.getKey()+", sample:"+sample.getDetails().getId());
					}
				}
			}
		}
	}
	
	private ArrayList<TemplateDetails> templatesToDetails(ArrayList<Template> templates, ArrayList<String> authorized) {
		ArrayList<TemplateDetails> details = new ArrayList<TemplateDetails>();
		for(Template t : templates)
			if(authorized == null || authorized.contains(t.getId())) 
				details.add(t.getDetails());
		return details;
	}
	
	private ArrayList<ProjectDetails> modelsToDetails(ArrayList<ProjectModel> models, ArrayList<String> authorized) {
		ArrayList<ProjectDetails> details = new ArrayList<ProjectDetails>();
		for(ProjectModel m : models)
			if(authorized == null || authorized.contains(m.getId())) 
				details.add(m.getDetails());
		return details;
	}
	
	@Override
	public Template getTemplate(String name) {
		for(ContentProvider<Template> templateProvider : templateProviders) {
			Template template = templateProvider.get(name);
			if(template!=null) return template;
		}
		return null;
	}
	
	@Override
	@RequiresUser
	public ArrayList<TemplateDetails> getTemplates() {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
		ArrayList<Template> templates = new ArrayList<Template>();
		for(ContentProvider<Template> templateProvider : templateProviders) {
			templates.addAll(templateProvider.list());
		}
		return templatesToDetails(templates, projectManager.getApplicationManager().getUser().getAuthorizedTemplate());
	}
	
	@Override
	@RequiresUser
	public HashMap<ProjectModel.Owner, ArrayList<ProjectDetails>> getModels() throws MissingUserException,
			ApplicationSecurityException {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
		
		HashMap<ProjectModel.Owner, ArrayList<ProjectDetails>> modelsMap = new HashMap<ProjectModel.Owner, ArrayList<ProjectDetails>>();
		
		// Get Application's project models
		ArrayList<ProjectModel> models = new ArrayList<ProjectModel>();
		for(ContentProvider<ProjectModel> modelProvider : modelProviders) {
			models.addAll(modelProvider.list());
		}
		modelsMap.put(ProjectModel.Owner.APPLICATION, modelsToDetails(models, projectManager.getApplicationManager().getUser().getAuthorizedModel()));
		
		// Get User's project models
		ContentProvider<ProjectModel> provider = userModelProviderFactory.get(projectManager.getApplicationManager().getUserProjectModelsDirectory());
		models = provider.list();
		modelsMap.put(ProjectModel.Owner.USER, modelsToDetails(models, null)); //We don't use filter on user's project models
		
		return modelsMap;
	}

	@Override
	public ArrayList<ProjectExporterDescriptor> getExporters() {
		ArrayList<ProjectExporterDescriptor> descriptors = new ArrayList<ProjectExporterDescriptor>();
		for(ProjectExporter exporter : exporters)
			descriptors.add(exporter.getDescriptor());
		return descriptors;
	}

	@Override
	@RequiresUser
	public boolean deleteModel(String id) throws MissingUserException,
			ApplicationSecurityException {

		// Get User's project models
		ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
		ContentProvider<ProjectModel> provider = userModelProviderFactory.get(projectManager.getApplicationManager().getUserProjectModelsDirectory());
		provider.list();
		return modelManager.deleteModel(provider, id);
	}

	@Override
	public Parameters getParameters() {
		if(securityEnabled) {
			Subject currentUser = SecurityUtils.getSubject();
			parameters.setConnected(currentUser != null);// && currentUser.isAuthenticated());
		}
		try { 
			User u = userProvider.getCurrentUser();
			parameters.setAuthor(new Author(u.getAuthorName(), authorEmail, authorUrl, authorCompany));
		}catch (MissingUserException e) {}
		return parameters;
	}

	@Override
	public Author getCurrentAutor() {
		User u = userProvider.getCurrentUser();
		return new Author(u.getAuthorName(), authorEmail, authorUrl, authorCompany);
	}

	@Override
	public List<LocaleDTO> supportedLocales() {
		return locales;
	}

	@Override
	public List<SampleDetails> getSamples(String module, String locale) {
		List<SampleDetails> details = null;
		Map<Locale, List<Sample>> locales = samples.get(module);
		if(locales != null){
			@SuppressWarnings("unchecked")
			List<Locale> localeLookUp = LocaleUtils.localeLookupList(determineLocaleKey(locale));
			List<Sample> samples = null;
			for(Locale l : localeLookUp) {
				logger.debug("Look up samples with locale:"+l);
				samples = locales.get(l);
				if(samples != null){
					logger.debug("Find some samples");
					break;
				}
			}
			if(samples != null){
				details = new ArrayList<SampleDetails>();
				for(Sample s : samples) {
					details.add(s.getDetails());
				}
			}
		}
		return details;
	}

	@Override
	public Map<String, Resource> installSampleInProject(ProjectDetails projectDetails, SampleDetails sampleDetails) {
		Map<String, Resource> theAddedResources = new HashMap<String, Resource>();
		Sample sample = getSample(sampleDetails);
		if(sample != null) {
			ProjectManager projectManager = projectManagerProvider.getProjectManager(projectDetails); 
			Project project = projectManager.get();
			if(sample.getResources() != null)
				for(Entry<String, Resource> resource : sample.getResources().entrySet()) {
					if(project.getResources().get(resource.getKey()) == null) {
						Resource r = resource.getValue();
						File resourceFile = getResourceFile(r, sample);
						if(resourceFile != null){
							r.setSize(resourceFile.length());
							projectManager.addResourceFile(resourceFile);
						}
						theAddedResources.put(resource.getKey(), projectManager.addResource(r));
					}
				}
		}
		return theAddedResources;
	}
	
	@Override
	public String getSampleContent(SampleDetails sampleDetails) {
		Sample sample = getSample(sampleDetails);
		if(sample != null)
			return sample.getContent();
		return null;
	}

	private File getResourceFile(Resource r, Sample s) {
		if(r != null && r instanceof IsFile && r.getUrl() != null)
			return new File(s.getResourceLocation(), r.getUrl());
		return null;
	}
	
	
	
	private Sample getSample(SampleDetails sampleDetails) {
		return (Sample) CollectionUtils.find(samples.get(sampleDetails.getModule()).get(determineLocaleKey(sampleDetails.getLocale())),
				PredicateUtils.transformedPredicate(TransformerUtils.invokerTransformer("getId"), PredicateUtils.equalPredicate(sampleDetails.getId())));
	}
	
	
}
