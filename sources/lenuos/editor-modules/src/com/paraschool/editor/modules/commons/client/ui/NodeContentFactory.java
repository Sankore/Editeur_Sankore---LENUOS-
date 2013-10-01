package com.paraschool.editor.modules.commons.client.ui;

import com.paraschool.editor.modules.commons.client.options.Option;

/*
 * Created at 4 oct. 2010
 * By bathily
 */
public interface NodeContentFactory<T extends NodeContent> {
	T get();
	Option[] getOptions();
}
