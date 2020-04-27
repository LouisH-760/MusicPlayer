package com.musicplayer.player;

import java.util.ArrayList;
import java.util.List;

import com.musicplayer.misc.Helper;

public class VLCJSinglePlayer implements Player {

	public final int MAX_VOLUME = 100;
	public final int MIN_VOLUME = 0;
	
	private final Runnable finishedAction;
	private Runnable positionUpdatedAction;
	private Runnable trackChanged;
	
	private List<VLCJSingleTrackPlayer> players;
	private int position;
	private int volumeIncr;
	private boolean playing;
	private int volume;
	
	public VLCJSinglePlayer() {
		players = new ArrayList<VLCJSingleTrackPlayer>();
		position = 0;
		volumeIncr = 5;
		playing = false;
		volume = 100;
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
		players.get(position).play();
	}

	@Override
	public void pause() {
		playing = false;
		players.get(position).pause();
	}

	@Override
	public void next() {
		playing = true;
		Helper.check(position < players.size(), "End of players reached!");
		position++;
		trackChanged.run();
		players.get(position).play();
		players.get(position - 1).stop();
	}

	@Override
	public void previous() {
		playing = true;
		Helper.check(position > 0, "End of players reached!");
		position--;
		trackChanged.run();
		players.get(position).play();
		players.get(position + 1).stop();
	}

	@Override
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
	public void volumeDown() {
		if(volume > MIN_VOLUME + volumeIncr)
			setVolume(volume - volumeIncr);
	}

	@Override
	public void volumeUp() {
		if(volume <= MAX_VOLUME - volumeIncr)
			setVolume(volume - volumeIncr);
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
	
	private void updateActions() {
		for(VLCJSingleTrackPlayer player : players) {
			player.setFinishedAction(finishedAction);
			player.setPositionUpdatedAction(positionUpdatedAction);
		}
	}
	
	public float getPosition() {
		return players.get(position).getPosition();
	}

	public void setPositionUpdatedAction(Runnable r) {
		positionUpdatedAction = r;
	}

}
