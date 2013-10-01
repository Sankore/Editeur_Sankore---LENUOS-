package com.paraschool.editor.api.client.ui.players;

import com.google.code.gwt.html5.media.client.Video;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 14 oct. 2010
 * By bathily
 */
public class HTML5VideoControls extends HTML5MediaControls {

	private static VideoControlsUiBinder uiBinder = GWT.create(VideoControlsUiBinder.class);
	@UiTemplate("VideoControls.ui.xml")
	interface VideoControlsUiBinder extends UiBinder<Widget, HTML5MediaControls> {}

	public HTML5VideoControls(Video video) {
		super(video);
	}

	@Override
	protected UiBinder<Widget, HTML5MediaControls> getUibinder() {
		return uiBinder;
	}

}
