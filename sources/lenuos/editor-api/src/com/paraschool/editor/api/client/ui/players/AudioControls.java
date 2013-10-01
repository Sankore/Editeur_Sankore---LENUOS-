package com.paraschool.editor.api.client.ui.players;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 14 oct. 2010
 * By bathily
 */
public class AudioControls extends MediaControls {

	private static AudioControlsUiBinder uiBinder = GWT.create(AudioControlsUiBinder.class);
	interface AudioControlsUiBinder extends UiBinder<Widget, AudioControls> {}

	
	public AudioControls(AudioPlayer.Player player) {
		super(player);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		play.setEnabled(false);
		
		bind();
	}

}
