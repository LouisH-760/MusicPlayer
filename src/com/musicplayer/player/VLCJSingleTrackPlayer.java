package com.musicplayer.player;

import com.musicplayer.misc.Helper;

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class VLCJSingleTrackPlayer {
	
	private String artist;
	private String album;
	private String title;
	private boolean ready;
	private AudioPlayerComponent mediaPlayerComponent;
	private Media song;
	private Runnable finishedAction;
	private Runnable positionUpdatedAction;
	
	public VLCJSingleTrackPlayer(String path) {
		mediaPlayerComponent = new AudioPlayerComponent();
		mediaPlayerComponent.mediaPlayer().media().prepare(path);
		mediaPlayerComponent.mediaPlayer().media().parsing().parse();
		ready = false;
		// What happens when media parsing is done
		mediaPlayerComponent.mediaPlayer().events().addMediaEventListener(new MediaEventAdapter() {
			public void mediaParsedChanged(Media media, MediaParsedStatus status) {
				 if(status == MediaParsedStatus.DONE) { 
					 // if it succeeded
					 artist = media.meta().get(Meta.ARTIST);
					 album = media.meta().get(Meta.ALBUM);
					 title = media.meta().get(Meta.TITLE);
					 ready = true;
					 song = media;
				 }					 
			 }
		});
		// playback event handling
		mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
		    @Override
		    public void finished(MediaPlayer mediaPlayer) {
		        finishedAction.run();
		    }

		    @Override
		    public void error(MediaPlayer mediaPlayer) {
		    	finishedAction.run();
		    }
		    
		    @Override
		    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
		    	if(positionUpdatedAction != null) {
		    		positionUpdatedAction.run();
		    	}
		    }
		});
	}
	
	public void play() {
		Helper.check(ready, "Song is not ready yet!");
		mediaPlayerComponent.mediaPlayer().media().play(song.newMediaRef());
		song.release();
	}
	
	public void pause() {
		mediaPlayerComponent.mediaPlayer().controls().pause();
	}
	
	public void stop() {
		mediaPlayerComponent.mediaPlayer().controls().stop();
	}
	
	public void release() {
		mediaPlayerComponent.release();
	}
	
	public void setVolume(int volume) {
		mediaPlayerComponent.mediaPlayer().audio().setVolume(volume);
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getTitle() {
		return title;
	}

	public void setFinishedAction(Runnable r) {
		finishedAction = r;
	}
	
	public long getDuration() {
		return mediaPlayerComponent.mediaPlayer().status().length();
	}
	
	public float getPosition() {
		return mediaPlayerComponent.mediaPlayer().status().position();
	}
	
	public void setPositionUpdatedAction(Runnable r) {
		positionUpdatedAction = r;
	}
}
