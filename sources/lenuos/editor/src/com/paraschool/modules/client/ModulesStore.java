package com.paraschool.modules.client;

import java.util.ArrayList;
import java.util.List;

import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.modules.associer.client.AssocierModule;
import com.paraschool.editor.modules.categoriser.client.CategoriserModule;
import com.paraschool.editor.modules.choisir.client.ChoisirModule;
import com.paraschool.editor.modules.etudier.client.EtudierModule;
import com.paraschool.editor.modules.ordonner.mot.client.OrdonnerModule;
import com.paraschool.editor.modules.selectionner.client.SelectionnerModule;

public class ModulesStore implements
		com.paraschool.editor.api.client.ModulesStore {

	private ArrayList<EditorModule> modules;
	
	private void addModule(EditorModule module) {
		module.init();
		modules.add(module);
	}
	
	@Override
	public List<EditorModule> getModules() {
		if(modules == null) {
			modules = new ArrayList<EditorModule>();
			addModule(new ChoisirModule());
			addModule(new SelectionnerModule());
			addModule(new EtudierModule());
			addModule(new AssocierModule());
			addModule(new CategoriserModule());
			addModule(new OrdonnerModule());
			addModule(new com.paraschool.editor.modules.ordonner.lettre.client.OrdonnerModule());
			addModule(new com.paraschool.editor.modules.ordonner.phrase.client.OrdonnerModule());
			addModule(new com.paraschool.editor.modules.ordonnerimg.client.OrdonnerModule());
			addModule(new com.paraschool.editor.modules.segmenter.mot.client.SegmenterModule());
			addModule(new com.paraschool.editor.modules.segmenter.lettre.client.SegmenterModule());
			addModule(new com.paraschool.editor.modules.segmenter.phrase.client.SegmenterModule());
		}
		
		return modules;
	}

}
