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
	// this is also shorter than a get-method
	private static final int TIMEOUT = 500;
	
	// action to run when a track finishes
	private final Runnable finishedAction;
	// action to run when the position changed
	private Runnable positionUpdatedAction;
	// action to update the GUI when metadata changes
	private Runnable trackChanged;
	
	private List<VLCJSingleTrackPlayer> players;
	private int position = 0;
	private int volumeIncr = DEFAULT_INCR;
	private boolean playing = false;
	private int volume = MAX_VOLUME;
	
	/**
	 * constructor: instanciate the player
	 */
	public VLCJSinglePlayer() {
		players = new ArrayList<VLCJSingleTrackPlayer>();
		finishedAction = () -> next();
	}

	@Override
	public void add(String song) {
		players.add(new VLCJSingleTrackPlayer(song));
		updateActions();
	}

	@Override
	public void addMultiple(List<String> songs) {
		for(String song : songs) {
			players.add(new VLCJSingleTrackPlayer(song));
		}
		updateActions();
	}

	@Override
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
	public void pause() {
		playing = false;
		players.get(position).pause();
	}

	@Override
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
	public void setVolume(int volume) {
		Helper.check(volume <= MAX_VOLUME, "You cannot set the volume this high");
		Helper.check(volume >= MIN_VOLUME, "You cannot set the volume this low");
		for(VLCJSingleTrackPlayer player : players) {
			player.setVolume(volume);;
		}
		this.volume = volume;
	}

	@Override
	public boolean isPaused() {
		return !(playing);
	}

	@Override
	public void setVolumeIncrement(int incr) {
		volumeIncr = incr;

	}

	@Override
	public int getVolumeIncrement() {
		return volumeIncr;
	}

	@Override
	public void setUpdateMediaAction(Runnable r) {
		trackChanged = r;
	}

	@Override
	public String nowPlayingTitle() {
		return players.get(position).getTitle();
	}

	@Override
	public String nowPlayingArtist() {
		return players.get(position).getArtist();
	}

	@Override
	public String nowPlayingAlbum() {
		return players.get(position).getAlbum();
	}

	@Override
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
	
	@Override
	public float getPosition() {
		return players.get(position).getPosition();
	}

	@Override
	public void setPositionUpdatedAction(Runnable r) {
		positionUpdatedAction = r;
	}
	
	@Override
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
