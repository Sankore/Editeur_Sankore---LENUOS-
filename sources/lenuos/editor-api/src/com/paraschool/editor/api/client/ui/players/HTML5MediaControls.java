package com.paraschool.editor.api.client.ui.players;

import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.google.code.gwt.html5.media.client.Media;
import com.google.code.gwt.html5.media.client.event.EndedEvent;
import com.google.code.gwt.html5.media.client.event.EndedHandler;
import com.google.code.gwt.html5.media.client.event.LoadedDataEvent;
import com.google.code.gwt.html5.media.client.event.LoadedDataHandler;
import com.google.code.gwt.html5.media.client.event.LoadedMetadataEvent;
import com.google.code.gwt.html5.media.client.event.LoadedMetadataHandler;
import com.google.code.gwt.html5.media.client.event.PauseEvent;
import com.google.code.gwt.html5.media.client.event.PauseHandler;
import com.google.code.gwt.html5.media.client.event.PlayEvent;
import com.google.code.gwt.html5.media.client.event.PlayHandler;
import com.google.code.gwt.html5.media.client.event.PlayingEvent;
import com.google.code.gwt.html5.media.client.event.PlayingHandler;
import com.google.code.gwt.html5.media.client.event.TimeUpdateEvent;
import com.google.code.gwt.html5.media.client.event.TimeUpdateHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

public abstract class HTML5MediaControls extends AbstractControls 
implements LoadedDataHandler, LoadedMetadataHandler, PlayHandler, PlayingHandler, PauseHandler, EndedHandler, TimeUpdateHandler {
	
	private Media media;
	
	public HTML5MediaControls(Media media) {
		super();
		initWidget(getUibinder().createAndBindUi(this));
		this.media = media;
		this.media.setControls(false);
		play.setEnabled(false);
		pause.setEnabled(false);
		bind();
		root.insert(media, 0);
		root.addStyleName(css.nativeMedia());
		root.addStyleName(css.stateLoading());
	}
	
	protected abstract UiBinder<Widget,HTML5MediaControls> getUibinder();
	
	protected void bind() {
		media.addLoadedDataHandler(this);
		media.addLoadedMetadataHandler(this);
		media.addPlayHandler(this);
		media.addPlayingHandler(this);
		media.addPauseHandler(this);
		media.addEndedHandler(this);
		media.addTimeUpdateHandler(this);
	}
	
	@UiHandler("play")
	protected void play(ClickEvent event) {
		if(!media.isPaused() && !media.hasEnded()){ // Hack à cause de qtwebkit dans Uniboard. Ce moteur ne supporte pas les évènements audio et video
			pause(null);
			return;
		}
		
		if(media.hasEnded()) { //Play après la fin d'un média ne relance pas l'évenement on play
			onPlay(null);
		}
		media.play();
	}

	@UiHandler("pause")
	protected void pause(ClickEvent event) {
		media.pause();
	}
	
	protected void setTime(long playPosition, long mediaDuration) {
		this.duration.setText(PlayerUtil.formatMediaTime(playPosition*1000));
		this.elapse.setText("-"+PlayerUtil.formatMediaTime((mediaDuration - playPosition)*1000));
		int percent = mediaDuration == 0 ? 0 : (int) (playPosition * 100 / mediaDuration);
		slider.setWidth(percent+"%");
	}
	
	protected void updateUI() {
		setTime((long)media.getCurrentTime(), (long)media.getDuration());
	}
	
	@Override
	public void onLoadedData(LoadedDataEvent event) {
		root.removeStyleName(css.stateLoading());
		play.setEnabled(true);
	}

	@Override
	public void onLoadedMetadata(LoadedMetadataEvent event) {
		setTime((long)media.getCurrentTime(), (long)media.getDuration()); 
	}
	
	@Override
	public void onEnded(EndedEvent event) {
		play.setEnabled(true);
		pause.setEnabled(false);
		root.removeStyleName(css.statePlaying());
		root.removeStyleName(css.statePausing());
		
	}
	
	@Override
	public void onPlay(PlayEvent event) {
		play.setEnabled(false);
		pause.setEnabled(true);
	}
	
	@Override
	public void onPlaying(PlayingEvent event) {
		root.removeStyleName(css.statePausing());
		root.addStyleName(css.statePlaying());
	}
	
	@Override
	public void onPause(PauseEvent event) {
		play.setEnabled(true);
		pause.setEnabled(false);
		root.removeStyleName(css.statePlaying());
		root.addStyleName(css.statePausing());
	}
	
	@Override
	public void onTimeUpdate(TimeUpdateEvent event) {
		updateUI();
	}
	
	@Override
	protected void play(double position) {
		media.setCurrentTime(media.getDuration() * position);
	}
}
