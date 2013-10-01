package com.paraschool.editor.server;

import com.paraschool.commons.share.Project;

/*
 * Created at 27 sept. 2010
 * By bathily
 */
public interface PublicationNotifier {
	void notify(User user, Project project, String url);
}
