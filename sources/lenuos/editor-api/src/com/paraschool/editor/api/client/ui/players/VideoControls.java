package com.paraschool.editor.api.client.ui.players;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 14 oct. 2010
 * By bathily
 */
public class VideoControls extends MediaControls {

	private static VideoControlsUiBinder uiBinder = GWT.create(VideoControlsUiBinder.class);
	interface VideoControlsUiBinder extends UiBinder<Widget, VideoControls> {}

	public VideoControls(VideoPlayer.Player player) {
		super(player);
		initWidget(uiBinder.createAndBindUi(this));
		play.setEnabled(false);
		bind();
		root.insert(player, 0);
	}
	

}
