package com.paraschool.editor.api.client.ui.players;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.CustomVideoPlayer;
import com.google.code.gwt.html5.media.client.Video;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;

/*
 * Created at 30 sept. 2010
 * By bathily
 */
public class VideoPlayer extends Composite {
	
	class Player extends CustomVideoPlayer {
		
		public Player(Plugin playerPlugin, String mediaURL, boolean autoplay, String height, String width)
				throws PluginNotFoundException, PluginVersionException,
				LoadException {
			super(playerPlugin, mediaURL, autoplay, height, width);
		}
		
		public Player(String mediaURL, boolean autoplay, String height, String width)
				throws PluginNotFoundException, PluginVersionException,
				LoadException {
			this(Plugin.Auto, mediaURL, autoplay, height, width);
		}
		
		public void error(String description) {
			fireError(description);
		}

		@Override
		protected void onVideoDimensionChanged(int width, int height) {
			
		}
	}
	public VideoPlayer(String mediaURL, boolean autoplay, String height, String width, String type) throws PluginNotFoundException, PluginVersionException, LoadException  {
		GWT.log(type);
		if(isHTML5Compliant(type))
			initWidget(new HTML5VideoControls(new Video(mediaURL)));
		else
			initWidget(new VideoControls(new Player(mediaURL, autoplay, height, width)));
	
	}
	
	private boolean isHTML5Compliant(String type) {
		return PlayerUtil.isHTML5CompliantClient() && type != null && !Video.canPlayType(type).equals("");
	}
	
}
