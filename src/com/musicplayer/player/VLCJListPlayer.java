package com.musicplayer.player;


import java.util.ArrayList;

import com.musicplayer.misc.Helper;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.PlaybackMode;

/**
 * Play media using VLCJ (libvlc api)
 * @author louis Hermier
 *
 */
public class VLCJListPlayer implements Player{
	private MediaPlayerFactory mediaPlayerFactory;
	private final EmbeddedMediaPlayer mediaPlayerComponent;
	public static final int MAX_VOLUME = 100;
	public static final int MIN_VOLUME = 0;
	public static final int NEXT_ITEM_TIMEOUT = 5000;// In milli secondes
	
	private Runnable mediaUpdateAction;
	
	private MediaListPlayer mediaListPlayer;
	private MediaList mediaList;
	private MediaListRef mediaListRef;
	private boolean paused;
	private int volume;
	private int incr = 5;
	
	private Media currentMedia;
	
	
	/**
	 * Create the listplayer, as well as the list where the media to play will be stored
	 * set the volume to the max as default in case it was changed by another application
	 */
	public VLCJListPlayer() {
		 mediaPlayerFactory = new MediaPlayerFactory();
		 mediaPlayerComponent = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
		 mediaListPlayer = mediaPlayerFactory.mediaPlayers().newMediaListPlayer();
		 mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayerComponent);
		 mediaList = mediaPlayerFactory.media().newMediaList();	 
		 paused = true;
		 mediaListPlayer.controls().setMode(PlaybackMode.LOOP);
		 // add a function that runs when the track changes
		 mediaListPlayer.events().addMediaListPlayerEventListener(new MediaListPlayerEventAdapter () {
			 @Override
			 public void nextItem(MediaListPlayer mediaListPlayer, MediaRef item) {
				 currentMedia = item.duplicateMedia(); // get a copy of the media that it is now playing to parse
				 currentMedia.parsing().parse(NEXT_ITEM_TIMEOUT);
				 // when it has finished parsing
				 currentMedia.events().addMediaEventListener(new MediaEventAdapter() {
					 public void mediaParsedChanged(Media media, MediaParsedStatus status) {
						 if(status == MediaParsedStatus.DONE) // if it succeeded
							 mediaUpdateAction.run(); // run the updates on the metadata
						 currentMedia.release(); // release the media anyway to free up RAM and not get garbage-collected into a crash
					 }
				 });
			 }
		 });
		 setVolume(MAX_VOLUME); // reset the volume when the player is created
	}
	
	/**
	 * Add a single song to the queue (using it's path)
	 * @param song : path to the audio file
	 */
	public void add(String song) {
		mediaListPlayer.mediaPlayer().mediaPlayer().media().prepare(song);
		mediaListPlayer.mediaPlayer().mediaPlayer().media().parsing().parse();
		mediaList.media().add(song);
		mediaListRef = mediaList.newMediaListRef();
		 try {
            mediaListPlayer.list().setMediaList(mediaListRef);
        } finally {
            mediaListRef.release();
        }
	}
	
	/**
	 * Add multiple songs to the queue (using their path)
	 * @param songs: arraylist of paths to the audio files
	 */
	public void addMultiple(ArrayList<String> songs) {
		for(String song : songs)
			add(song);
	}
	
	/**
	 * Start playing the list (will loop at the end)
	 */
	public void play() {
		paused = false;
		mediaListPlayer.controls().play();
	}
	
	/**
	 * pause playback
	 */
	public void pause() {
		paused = true;
		mediaListPlayer.controls().pause();
	}
	
	/**
	 * go to next song. If playback is paused, resume.
	 */
	public void next() {
		paused = false;
		mediaListPlayer.controls().playNext();
	}
	
	/**
	 * Go to previous song. If playback is paused, resume.
	 */
	public void previous() {
		paused = false;
		mediaListPlayer.controls().playPrevious();
	}
	
	/**
	 * Stop playback. Unused in the ui for now
	 */
	public void stop() {
		mediaListPlayer.controls().stop();
		mediaListPlayer.release(); // once the player is stopped, release it to free up ram
		// it is not supposed to be playing again after that...
	}
	
	/**
	 * set the volume to a precise int, between the minimum and maximum set in the class constants. Constants are public.
	 */
	public void setVolume(int volume) {
		Helper.check(volume <= MAX_VOLUME, "You cannot set the volume this high");
		Helper.check(volume >= MIN_VOLUME, "You cannot set the volume this low");
		mediaListPlayer.mediaPlayer().mediaPlayer().audio().setVolume(volume);
		this.volume = volume;
		System.out.println("New volume: " + volume);
	}
	
	/**
	 * Increment the volume by a certain value
	 * available through getVolumeIncrement(), default is 5
	 * edit it through setVolumeIncrement
	 */
	public void volumeUp() {
		if(volume <= MAX_VOLUME - incr)
			setVolume(volume + incr);
	}
	
	/**
	 * decrement the volume by a certain value
	 * available through getVolumeIncrement(), default is 5
	 * edit it through setVolumeIncrement
	 */
	public void volumeDown() {
		if(volume > MIN_VOLUME + incr)
			setVolume(volume - incr);
	}

	/**
	 * Checks if playback is paused
	 * useful if you want to have only one play/pause button
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * set the amount by which the volume is modified
	 * affects volumeUp() and volumeDown()
	 */
	public void setVolumeIncrement(int incr) {
		this.incr = incr;
	}
	
	/**
	 * get the increment of the volume
	 */
	public int getVolumeIncrement() {
		return incr;
	}
	
	/**
	 * Updates the function that is executed when the metadata for the new playing media becomes available
	 * @param r > ideally a lambda function that uses the functions below to get their info
	 */
	public void setUpdateMediaAction(Runnable r) {
		mediaUpdateAction = r;
	}
	
	/**
	 * get the title of the song currently playing
	 */
	public String nowPlayingTitle() {
		return currentMedia.meta().get(Meta.TITLE);
	}
	
	/**
	 * get the artist of the song currently playing
	 */
	public String nowPlayingArtist() {
		return currentMedia.meta().get(Meta.ARTIST);
	}
	
	/**
	 * get the album of the song currently playing
	 */
	public String nowPlayingAlbum() {
		return currentMedia.meta().get(Meta.ALBUM);
	}
	
}
