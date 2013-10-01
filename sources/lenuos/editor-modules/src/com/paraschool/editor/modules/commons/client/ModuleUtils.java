package com.paraschool.editor.modules.commons.client;

import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.EditModuleContext.Mode;

public final class ModuleUtils {

	public static boolean canEditOptions(EditModuleContext context) {
		EditModuleContext.Mode mode = context.getMode();
		return mode == Mode.FULL || mode == Mode.LIGHT;
	}
	
	public static boolean canDeleteModule(EditModuleContext context) {
		EditModuleContext.Mode mode = context.getMode();
		return mode == Mode.FULL;
	}
	
	public static boolean canAddOrRemoveContent(EditModuleContext context) {
		EditModuleContext.Mode mode = context.getMode();
		return mode == Mode.FULL;
	}
	
	public static boolean canChangeContent(EditModuleContext context) {
		EditModuleContext.Mode mode = context.getMode();
		return mode == Mode.FULL || mode == Mode.LIGHT || mode == Mode.ONLY_MEDIA;
	}
}
