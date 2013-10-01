package com.paraschool.editor.server.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.paraschool.editor.server.Viewers;
import com.paraschool.editor.server.exporter.CompositePackageProjectExporter;
import com.paraschool.editor.server.exporter.CompositePackageProjectExporterFactory;
import com.paraschool.editor.server.exporter.PSProjectExporter;
import com.paraschool.editor.server.exporter.PackageProjectExporter;
import com.paraschool.editor.server.exporter.SCORMProjectExporter;
import com.paraschool.editor.server.exporter.W3CWidgetProjectExporter;

public class ProjectExporterModule extends AbstractModule {

	@Override
	protected void configure() {

		Multibinder<PackageProjectExporter> packageExporterBinder = Multibinder.newSetBinder(binder(), PackageProjectExporter.class);
		packageExporterBinder.addBinding().to(W3CWidgetProjectExporter.class);
		packageExporterBinder.addBinding().to(SCORMProjectExporter.class);
		packageExporterBinder.addBinding().to(PSProjectExporter.class);

		install(new FactoryModuleBuilder().implement(CompositePackageProjectExporter.class, CompositePackageProjectExporter.class).build(CompositePackageProjectExporterFactory.class));
		
		//bind(CompositePackageProjectExporterFactory.class).toProvider(FactoryProvider.newFactory(CompositePackageProjectExporterFactory.class, CompositePackageProjectExporter.class));
		
		requestStaticInjection(Viewers.class);
	}

}
