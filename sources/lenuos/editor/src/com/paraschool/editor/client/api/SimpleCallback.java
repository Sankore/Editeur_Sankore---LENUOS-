package com.paraschool.editor.client.api;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

/*
 * Created at 7 nov. 2010
 * By bathily
 */
@Export
@ExportClosure
public interface SimpleCallback extends Exportable {
	void call();
}
