package com.musicplayer.player;

import java.util.List;

/**
 * Defines the minimum framework to implement a player to be a drop-in replacement in Main.
 * see VLCJListPlayer for an example of implementation and detail about each function
 * @author louis Hermier
 *
 */
public interface Player {
	
	public static final int MAX_VOLUME = 100;
	public static final int MIN_VOLUME = 0;
	
	// add a single song to the play queue
	// (can technically be done after starting playing)
	public void add(String song);
	// add multiple songs to the play queue
	// (can technically be done after starting playing)
	public void addMultiple(List<String> songs);
	// start / resume playback
	public void play();
	// pause playback
	public void pause();
	// jump to song after current one in the queue
	// if paused, resume
	public void next();
	// jump to song before the current one in the queue
	// if paused, resume
	public void previous();
	// completely stop playback.
	// implies freeing everything used for playback as much as possible
	public void stop();
	// set the playback volume
	// ideally from 0 - 100 for consistency
	public void setVolume(int volume);
	// is playback paused?
	public boolean isPaused();
	// decrement the volume by a set amount
	public void volumeDown();
	// increment the volume by a set amount
	public void volumeUp();
	// set the amount by which the volume is incremented and decremented
	public void setVolumeIncrement(int incr);
	// get the amount by which the volume is incremented or decremented
	public int getVolumeIncrement();
	// set what will be run when the metadata for the new media playing will become available
	public void setUpdateMediaAction(Runnable r);
	// get the title of the current playing media
	public String nowPlayingTitle();
	// get the artist of the current playing media
	public String nowPlayingArtist();
	// get the album of the current playing media
	public String nowPlayingAlbum();
	// get the current volume
	public int getVolume();
	// set what happens when the playback position is updated.
	public void setPositionUpdatedAction(Runnable r);
	// set the playback position
	public void setPosition(float position);
	// get the playback position
	public float getPosition();
	// get the track duration (long, in milliseconds)
	public long getDuration();
}
