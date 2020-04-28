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
	private Runnable finishedAction;
	private Runnable positionUpdatedAction;
	
	/**
	 * create a new instance of a player that will only play a single track
	 * @param path > path to the media
	 */
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
					 media.release();
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
	
	/**
	 * Start / resume playback
	 * throws an exception if parsing isn't done
	 */
	public void play() {
		Helper.check(ready, "Song is not ready yet!");
		mediaPlayerComponent.mediaPlayer().controls().play();
	}
	
	/**
	 * Pause playback
	 */
	public void pause() {
		mediaPlayerComponent.mediaPlayer().controls().pause();
	}
	
	/**
	 * stop playback
	 */
	public void stop() {
		mediaPlayerComponent.mediaPlayer().controls().stop();
	}
	
	/**
	 * free all the ressources
	 */
	public void release() {
		if(song != null) {
			song.release();
		}
		mediaPlayerComponent.release();
	}
	
	/**
	 * set the volume precisely
	 * @param volume
	 */
	public void setVolume(int volume) {
		mediaPlayerComponent.mediaPlayer().audio().setVolume(volume);
	}
	
	/**
	 * get the current media's artist
	 * @return artist
	 */
	public String getArtist() {
		return artist;
	}
	
	// the three following functions return null if the parsing is not done
	/**
	 * get the current media's album
	 * @return album
	 */
	public String getAlbum() {
		return album;
	}
	
	/**
	 * return the current media's title
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * set what to run once the song finishes
	 * @param r
	 */
	public void setFinishedAction(Runnable r) {
		finishedAction = r;
	}
	
	/**
	 * get the duration of the song
	 * @return duration (long, in milliseconds)
	 */
	public long getDuration() {
		return mediaPlayerComponent.mediaPlayer().status().length();
	}
	
	/**
	 * get the current playback position
	 * @return float between 0 (beginning) and 1 (end)
	 */
	public float getPosition() {
		return mediaPlayerComponent.mediaPlayer().status().position();
	}
	
	/**
	 * what to run when the position updates
	 * @param r
	 */
	public void setPositionUpdatedAction(Runnable r) {
		positionUpdatedAction = r;
	}
	
	/**
	 * set the playback position
	 * @param position float between 0 (beginning) and 1 (end)
	 */
	public void setPosition(float position) {
		mediaPlayerComponent.mediaPlayer().controls().setPosition(position);
	}
}
