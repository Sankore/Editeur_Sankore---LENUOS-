package com.paraschool.editor.api.client.ui.players;

import com.google.code.gwt.html5.media.client.Audio;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 14 oct. 2010
 * By bathily
 */
public class HTML5AudioControls extends HTML5MediaControls {

	private static AudioControlsUiBinder uiBinder = GWT.create(AudioControlsUiBinder.class);
	@UiTemplate("AudioControls.ui.xml")
	interface AudioControlsUiBinder extends UiBinder<Widget, HTML5MediaControls> {}

	
	public HTML5AudioControls(Audio audio) {
		super(audio);
	}

	@Override
	protected UiBinder<Widget, HTML5MediaControls> getUibinder() {
		return uiBinder;
	}

}
