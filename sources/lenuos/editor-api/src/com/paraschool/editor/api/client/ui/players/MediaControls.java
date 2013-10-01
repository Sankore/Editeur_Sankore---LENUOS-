package com.paraschool.editor.api.client.ui.players;

/*
 * Created at 14 oct. 2010
 * By bathily
 */
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiHandler;

public abstract class MediaControls extends AbstractControls {

	protected enum PlayState {
		Playing, Pause, Stop;
	}

	private PlayState playState = PlayState.Stop;
	protected final AbstractMediaPlayer player;

	public MediaControls(AbstractMediaPlayer player) {
		super();
		this.player = player;
	}

	protected void updateUI() {
		setTime((long)MediaControls.this.player.getPlayPosition(), MediaControls.this.player.getMediaDuration());
	}
	
	protected void bind() {

		player.addLoadingProgressHandler(new LoadingProgressHandler() {
			@Override
			public void onLoadingProgress(LoadingProgressEvent event) {
				double prog = event.getProgress();
				if (prog == 1.0) {
					root.removeStyleName(css.stateLoading());
					setTime((long)player.getPlayPosition(), player.getMediaDuration());
				}else
					root.addStyleName(css.stateLoading());
			}
		});

		player.addPlayerStateHandler(new PlayerStateHandler() {

			@Override
			public void onPlayerStateChanged(PlayerStateEvent event) {
				switch (event.getPlayerState()) {
				case BufferingFinished:
				case BufferingStarted:
					toPlayState(PlayState.Pause);
					break;
				case Ready:
					toPlayState(PlayState.Stop);
					break;
				}
			}
		});

		player.addPlayStateHandler(new PlayStateHandler() {

			@Override
			public void onPlayStateChanged(PlayStateEvent event) {
				switch (event.getPlayState()) {
				case Stopped:
				case Finished:
					toPlayState(PlayState.Stop);
					break;
				case Paused:
					toPlayState(PlayState.Pause);
					break;
				case Started:
					toPlayState(PlayState.Playing);
					break;
				}
			}
		});

	}

	@UiHandler("play")
	protected void play(ClickEvent event) {
		switch (playState) {
		case Stop:
		case Pause:
			try {
				player.playMedia();
			} catch (Throwable ex) {
				//player.error(ex.getMessage());
			}
			break;
		default:
			break;
		}
	}

	@UiHandler("pause")
	protected void pause(ClickEvent event) {
		if(playState == PlayState.Playing){
			try {
				player.pauseMedia();
			} catch (Throwable ex) {
				GWT.log(ex.getMessage());
				//player.error(ex.getMessage());
			}
		}

	}

	@Override
	protected void play(double position) {
		long pos = (long) (player.getMediaDuration() * position);
		player.setPlayPosition(pos);
		setTime(pos, player.getMediaDuration());
	}

	private void toPlayState(PlayState state) {
		GWT.log(state+"");
		switch (state) {
		case Playing:
			play.setEnabled(false);
			pause.setEnabled(true);
			root.removeStyleName(css.statePausing());
			root.addStyleName(css.statePlaying());
			launchTimer();
			break;
		case Stop:
			stopTimer();
			try {
				setTime(0, player.getMediaDuration());
			} catch (Exception e) {
			}
			play.setEnabled(true);
			pause.setEnabled(false);
			root.removeStyleName(css.statePlaying());
			root.removeStyleName(css.statePausing());
			break;
		case Pause:
			stopTimer();
			play.setEnabled(true);
			pause.setEnabled(false);
			root.removeStyleName(css.statePlaying());
			root.addStyleName(css.statePausing());
			break;
		}
		playState = state;
	}

	protected void setTime(long playPosition, long mediaDuration) {

		this.duration.setText(PlayerUtil.formatMediaTime(playPosition));
		this.elapse.setText("-"+PlayerUtil.formatMediaTime(mediaDuration - playPosition));
		int percent = mediaDuration == 0 ? 0 : (int) (playPosition * 100 / mediaDuration);

		slider.setWidth(percent+"%");
		
	}

}