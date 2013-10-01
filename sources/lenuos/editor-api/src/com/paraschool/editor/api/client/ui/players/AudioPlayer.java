package com.paraschool.editor.api.client.ui.players;

import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.CustomAudioPlayer;
import com.google.code.gwt.html5.media.client.Audio;
import com.google.gwt.user.client.ui.Composite;

/*
 * Created at 30 sept. 2010
 * By bathily
 */
public class AudioPlayer extends Composite {
	
	class Player extends CustomAudioPlayer {
		
		private final AbstractControls controls;
		
		public Player(Plugin playerPlugin, String mediaURL, boolean autoplay)
				throws PluginNotFoundException, PluginVersionException,
				LoadException {
			super(playerPlugin, mediaURL, autoplay, "0", "0");
			this.controls = new AudioControls(this);
			setPlayerControlWidget(controls);
		}
		
		public Player(String mediaURL, boolean autoplay)
				throws PluginNotFoundException, PluginVersionException,
				LoadException {
			this(Plugin.Auto, mediaURL, autoplay);
		}
		
		public void error(String description) {
			fireError(description);
		}
	}
	
	public AudioPlayer(String mediaURL, boolean autoplay, String type) throws PluginNotFoundException, PluginVersionException, LoadException  {
		if(isHTML5Compliant(type))
			initWidget(new HTML5AudioControls(new Audio(mediaURL)));
		else
			initWidget(new Player(mediaURL, autoplay));
	}
	
	private boolean isHTML5Compliant(String type) {
		return PlayerUtil.isHTML5CompliantClient() && type != null && !Audio.canPlayType(type).equals("");
	}
	
}
