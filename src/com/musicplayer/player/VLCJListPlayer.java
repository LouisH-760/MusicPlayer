package com.musicplayer.player;


import java.util.List;

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
	public static final int NEXT_ITEM_TIMEOUT = 5000;// In milli secondes
	
	private Runnable mediaUpdateAction;
	
	private MediaListPlayer mediaListPlayer;
	private MediaList mediaList;
	private MediaListRef mediaListRef;
	private boolean paused;
	private int volume;
	private int incr = DEFAULT_INCR;
	
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
	
	@Override
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
	
	@Override
	public void addMultiple(List<String> songs) {
		for(String song : songs)
			add(song);
	}
	
	@Override
	public void play() {
		paused = false;
		mediaListPlayer.controls().play();
	}
	
	@Override
	public void pause() {
		paused = true;
		mediaListPlayer.controls().pause();
	}
	
	
	@Override
	public void next() {
		paused = false;
		mediaListPlayer.controls().playNext();
	}
	
	@Override
	public void previous() {
		paused = false;
		mediaListPlayer.controls().playPrevious();
	}
	
	/**
	 * Stop playback. Unused in the ui for now
	 */
	@Override
	public void stop() {
		mediaListPlayer.controls().stop();
		mediaListPlayer.release(); // once the player is stopped, release it to free up ram
		// it is not supposed to be playing again after that...
	}
	
	@Override
	public void setVolume(int volume) {
		Helper.check(volume <= MAX_VOLUME, "You cannot set the volume this high");
		Helper.check(volume >= MIN_VOLUME, "You cannot set the volume this low");
		mediaListPlayer.mediaPlayer().mediaPlayer().audio().setVolume(volume);
		this.volume = volume;
	}
	
	@Override
	public int getVolume() {
		return volume;
	}
	
	@Override
	public boolean isPaused() {
		return paused;
	}

	@Override
	public void setVolumeIncrement(int incr) {
		this.incr = incr;
	}
	
	@Override
	public int getVolumeIncrement() {
		return incr;
	}
	
	@Override
	public void setUpdateMediaAction(Runnable r) {
		mediaUpdateAction = r;
	}
	
	@Override
	public String nowPlayingTitle() {
		return currentMedia.meta().get(Meta.TITLE);
	}
	
	@Override
	public String nowPlayingArtist() {
		return currentMedia.meta().get(Meta.ARTIST);
	}
	
	@Override
	public String nowPlayingAlbum() {
		return currentMedia.meta().get(Meta.ALBUM);
	}

	@Override
	public void setPositionUpdatedAction(Runnable r) {
		// This doesn't do anything, as this player can't report the playback position.
	}

	@Override
	public void setPosition(float position) {
		// this cannot be implemented due to the limitations of this player
	}

	@Override
	public float getPosition() {
		// this cannot be implemented due to the limitations of this player
		return 0;
	}

	@Override
	public long getDuration() {
		// this cannot be implemented due to the limitations of this player
		return 0;
	}
	
}
