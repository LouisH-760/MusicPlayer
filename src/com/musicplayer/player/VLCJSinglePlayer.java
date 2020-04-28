package com.musicplayer.player;

import java.util.ArrayList;
import java.util.List;

import com.musicplayer.misc.Helper;

/**
 * Implements the player interface by using a player per track instead
 * @author louis Hermier
 *
 */
public class VLCJSinglePlayer implements Player {
	// volume is public in case the logic needs to access it.
	// this is also shorter than a get-method
	public final int MAX_VOLUME = 100;
	public final int MIN_VOLUME = 0;
	private final int TIMEOUT = 500;
	public final String BOUNDARY_REACHED = "Playlist boundary reached.";
	
	// action to run when a track finishes
	private final Runnable finishedAction;
	// action to run when the position changed
	private Runnable positionUpdatedAction;
	// action to update the GUI when metadata changes
	private Runnable trackChanged;
	
	private List<VLCJSingleTrackPlayer> players;
	private int position;
	private int volumeIncr;
	private boolean playing;
	private int volume;
	
	/**
	 * constructor: instanciate the player
	 */
	public VLCJSinglePlayer() {
		players = new ArrayList<VLCJSingleTrackPlayer>();
		position = 0;
		volumeIncr = 5;
		playing = false;
		volume = 100;
		finishedAction = () -> next();
	}

	@Override
	/**
	 * Add a single song to the queue
	 * @param song > path to the song
	 */
	public void add(String song) {
		players.add(new VLCJSingleTrackPlayer(song));
		updateActions();
	}

	@Override
	/**
	 * Add multiple songs to the queue
	 * @param List of the songs to add
	 */
	public void addMultiple(List<String> songs) {
		for(String song : songs) {
			players.add(new VLCJSingleTrackPlayer(song));
		}
		updateActions();
	}

	@Override
	/**
	 * Start / resume playback
	 */
	public void play() {
		playing = true;
		trackChanged.run();
		try {
			players.get(position).play();
		} catch (IllegalArgumentException e) {
			System.out.println("Track not ready yet, Waiting.");
			try {
				Thread.sleep(TIMEOUT);
			} catch (InterruptedException e1) {
				System.out.println("Interrupted");
			}
			play();
		}
		
	}

	@Override
	/**
	 * pause playback
	 */
	public void pause() {
		playing = false;
		players.get(position).pause();
	}

	@Override
	/**
	 * skip to the next track
	 */
	public void next() {
		if (position < players.size() - 1)
		{
			playing = true;
			position++;
			trackChanged.run();
			players.get(position).play();
			players.get(position - 1).stop();
		} else {
			System.out.println(BOUNDARY_REACHED);
		}
	}

	@Override
	/**
	 * skip to the previous track
	 */
	public void previous() {
		if (position > 0) {
			playing = true;
			position--;
			trackChanged.run();
			players.get(position).play();
			players.get(position + 1).stop();
		} else {
			System.out.println(BOUNDARY_REACHED);
		}
	}

	@Override
	/**
	 * Stop playback completely and free ressources
	 */
	public void stop() {
		players.get(position).stop();
		for(VLCJSingleTrackPlayer player : players) {
			player.release();
		}
	}

	@Override
	/**
	 * set the volume precisely
	 */
	public void setVolume(int volume) {
		Helper.check(volume <= MAX_VOLUME, "You cannot set the volume this high");
		Helper.check(volume >= MIN_VOLUME, "You cannot set the volume this low");
		for(VLCJSingleTrackPlayer player : players) {
			player.setVolume(volume);;
		}
		this.volume = volume;
	}

	@Override
	/**
	 * is playback paused?
	 */
	public boolean isPaused() {
		return !(playing);
	}

	@Override
	/**
	 * decrease the volume by a given increment
	 */
	public void volumeDown() {
		if(volume > MIN_VOLUME + volumeIncr)
			setVolume(volume - volumeIncr);
	}

	@Override
	/**
	 * increase the volume by a given increment
	 */
	public void volumeUp() {
		if(volume <= MAX_VOLUME - volumeIncr)
			setVolume(volume - volumeIncr);
	}

	@Override
	/**
	 * set the increment by which to increase / decrease the volume
	 */
	public void setVolumeIncrement(int incr) {
		volumeIncr = incr;

	}

	@Override
	/**
	 * get the increment by which the volume is increased / decreased
	 */
	public int getVolumeIncrement() {
		return volumeIncr;
	}

	@Override
	/**
	 * set what happens when the media metadata is updated
	 */
	public void setUpdateMediaAction(Runnable r) {
		trackChanged = r;
	}

	@Override
	/**
	 * get the title of the now playing media
	 */
	public String nowPlayingTitle() {
		return players.get(position).getTitle();
	}

	@Override
	/**
	 * get the artist of the now playing media
	 */
	public String nowPlayingArtist() {
		return players.get(position).getArtist();
	}

	@Override
	/**
	 * get the album of the now playing media
	 */
	public String nowPlayingAlbum() {
		return players.get(position).getAlbum();
	}

	@Override
	/**
	 * get the current volume
	 */
	public int getVolume() {
		return volume;
	}
	
	/**
	 * set the actions for newly added media
	 */
	private void updateActions() {
		for(VLCJSingleTrackPlayer player : players) {
			player.setFinishedAction(finishedAction);
			player.setPositionUpdatedAction(positionUpdatedAction);
		}
	}
	
	/**
	 * get the playback position
	 * @return position: float between 0 (beginning) and 1 (end)
	 */
	public float getPosition() {
		return players.get(position).getPosition();
	}

	/**
	 * what runs when the position is updated?
	 * @param r
	 */
	public void setPositionUpdatedAction(Runnable r) {
		positionUpdatedAction = r;
	}
	
	/**
	 * set the playback position
	 * @param position: float between 0 (beginnning) and 1 (end)
	 */
	public void setPosition(float position) {
		Helper.check(position <= 1, "Position can't be higher than 1");
		Helper.check(position >= 0, "Position can't be lower than 0");
		players.get(this.position).setPosition(position);
	}

	@Override
	public long getDuration() {
		return players.get(position).getDuration();
	}

}
