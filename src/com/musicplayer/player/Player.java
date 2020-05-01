package com.musicplayer.player;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Defines the minimum framework to implement a player to be a drop-in replacement in Main.
 * see VLCJListPlayer for an example of implementation and detail about each function
 * @author louis Hermier
 *
 */
public interface Player {
	// volume is public in case the logic needs to access it.
	public static final int MAX_VOLUME = 100;
	public static final int MIN_VOLUME = 0;
	public static final int DEFAULT_INCR = 5;
	public static final String BOUNDARY_REACHED = "Playlist boundary reached.";
	
	/**
	 * Add a single song to the queue (using it's path)
	 * (can technically be done after starting playing)
	 * @param song : path to the audio file
	 */
	public void add(String song);
	/**
	 * Add multiple songs to the queue (using their path)
	 * (can technically be done after starting playing)
	 * @param songs: arraylist of paths to the audio files
	 */
	public void addMultiple(List<String> songs);
	/**
	 * Start playing the list (will loop at the end)
	 */
	public void play();
	/**
	 * Pause playback
	 */
	public void pause();
	/**
	 * Jump to song after current one in the queue. If playback is paused, resume.
	 */
	public void next();
	/**
	 * Jump to song before the current one in the queue. If playback is paused, resume.
	 */
	public void previous();
	// Completely stop playback.
	// Implies freeing everything used for playback as much as possible
	public void stop();
	/**
	 * Set the volume to a precise int, between the minimum and maximum set in the class constants.
	 */
	public void setVolume(int volume);
	/**
	 * Checks if playback is paused
	 * Useful if you want to have only one play/pause button
	 */
	public boolean isPaused();
	/**
	 * Decrement the volume by a certain value
	 * Available through getVolumeIncrement
	 * Edit it through setVolumeIncrement
	 */
	public default void volumeDown() {
		int newVolume = getVolume() - getVolumeIncrement();
		if(newVolume >= MIN_VOLUME)
			setVolume(newVolume);
	}
	/**
	 * Increment the volume by a certain value
	 * Available through getVolumeIncrement()
	 * Edit it through setVolumeIncrement
	 */
	public default void volumeUp() {
		int newVolume = getVolume() + getVolumeIncrement();
		if(newVolume <= MAX_VOLUME)
			setVolume(newVolume);
	}
	/**
	 * Set the amount by which the volume is modified
	 * Affects volumeUp() and volumeDown()
	 */
	public void setVolumeIncrement(int incr);
	/**
	 * Get the increment of the volume
	 */
	public int getVolumeIncrement();
	/**
	 * Updates the function that is executed when the metadata for the new playing media becomes available
	 * @param r > ideally a lambda function that uses the functions below to get their info
	 */
	public void setUpdateMediaAction(Runnable r);
	/**
	 * Get the title of the song currently playing
	 */
	public String nowPlayingTitle();
	// get the artist of the current playing media
	/**
	 * get the artist of the song currently playing
	 */
	public String nowPlayingArtist();
	/**
	 * Get the album of the song currently playing
	 */
	public String nowPlayingAlbum();
	/**
	 * *Get the volume level
	 * @return volume
	 */
	public int getVolume();
	/**
	 * What runs when the position is updated?
	 * @param r
	 */
	public void setPositionUpdatedAction(Runnable r);
	/**
	 * Set the playback position
	 * @param position: float between 0 (beginnning) and 1 (end)
	 */
	public void setPosition(float position);
	/**
	 * Get the playback position
	 * @return position: float between 0 (beginning) and 1 (end)
	 */
	public float getPosition();
	/**
	 * Get the duration of the current track (long, in milliseconds)
	 */
	public long getDuration();
	/**
	 * get an url to the embedded artwork
	 *  (for the VLCJ player, this is a path to a cached copy of the artwork)
	 * @return : URI to the album art
	 * @throws URISyntaxException in case the URI is invalid
	 */
	public URI getEmbeddedCoverUri() throws URISyntaxException;
}
