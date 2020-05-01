package com.musicplayer.player;

import java.util.ArrayList;
import java.util.List;

import com.musicplayer.misc.Helper;

import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

/**
 * @author Vincent Carpentier
 * @deprecated unfinished
 */
@Deprecated
public class VLCJBufferedPlayer implements Player {

	private List<String> songs = new ArrayList<String>();
	private AudioPlayerComponent[] playerBuffer;
	private Runnable finishedAction;
	private Runnable positionUpdatedAction;
	private int bufferPosition;
	private int songsPosition;
	private int endPosition = 0;
	private int incr = DEFAULT_INCR;
	
	public VLCJBufferedPlayer(int size) {
		playerBuffer = new AudioPlayerComponent[size];
		for (int i = 0; i < size; i++) {
			playerBuffer[i] = new AudioPlayerComponent();
			playerBuffer[i].mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			    @Override
			    public void finished(MediaPlayer mediaPlayer) {
			    	new Thread(finishedAction).start();
			    }
	
			    @Override
			    public void error(MediaPlayer mediaPlayer) {
			    	new Thread(finishedAction).start();
			    }
			    
			    @Override
			    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
			    	if(positionUpdatedAction != null) {
			    		positionUpdatedAction.run();
			    	}
			    }
			});
		}
	}
	
	private void updtadeSongsBuffer() {
		if (endPosition != -1) {
			int i = 0;
			if (endPosition == bufferPosition &&  songs.size() > (i = songsPosition + ((endPosition > bufferPosition) ? endPosition - bufferPosition : endPosition + playerBuffer.length - bufferPosition))) {
				loadSong(i, endPosition++);
				endPosition %= playerBuffer.length;
			}
			while ((endPosition - bufferPosition) % playerBuffer.length != 0 &&  songs.size() > (i = songsPosition + ((endPosition > bufferPosition) ? endPosition - bufferPosition : endPosition + playerBuffer.length - bufferPosition))) {
				loadSong(i, endPosition++);
				endPosition %= playerBuffer.length;
				System.out.println("a");
			}
			if ((endPosition - bufferPosition) % playerBuffer.length != 0)
				endPosition = -1;
		}
	}
	
	private void loadSong(int songPosition, int bufferPosition) {
		playerBuffer[bufferPosition].mediaPlayer().media().prepare(songs.get(songPosition));
	}
	
	@Override
	public void add(String song) {
		songs.add(song);
		updtadeSongsBuffer();
	}

	@Override
	public void addMultiple(List<String> songs) {
		this.songs.addAll(songs);
		updtadeSongsBuffer();
	}

	@Override
	public void play() {
		playerBuffer[bufferPosition].mediaPlayer().controls().play();
	}

	@Override
	public void pause() {
		playerBuffer[bufferPosition].mediaPlayer().controls().pause();
	}

	@Override
	public void next() {
		stop();
		if (songsPosition < songs.size()-1) {
			if (endPosition == -1)
				endPosition = bufferPosition;
			bufferPosition = (songsPosition + 1) % playerBuffer.length;
			updtadeSongsBuffer();
			play();
		}
	}

	@Override
	public void previous() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop() {
		playerBuffer[bufferPosition].mediaPlayer().controls().stop();
	}

	@Override
	public void setVolume(int volume) {
		Helper.check(volume <= MAX_VOLUME, "You cannot set the volume this high");
		Helper.check(volume >= MIN_VOLUME, "You cannot set the volume this low");
		playerBuffer[bufferPosition].mediaPlayer().audio().setVolume(volume);
	}

	@Override
	public boolean isPaused() {
		return !playerBuffer[bufferPosition].mediaPlayer().status().isPlaying();
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
		positionUpdatedAction = r;
	}

	@Override
	public String nowPlayingTitle() {
		return (playerBuffer[bufferPosition].mediaPlayer().media().meta() == null) ? "" : playerBuffer[bufferPosition].mediaPlayer().media().meta().get(Meta.TITLE);
	}

	@Override
	public String nowPlayingArtist() {
		return (playerBuffer[bufferPosition].mediaPlayer().media().meta() == null) ? "" : playerBuffer[bufferPosition].mediaPlayer().media().meta().get(Meta.ARTIST);
	}

	@Override
	public String nowPlayingAlbum() {
		return (playerBuffer[bufferPosition].mediaPlayer().media().meta() == null) ? "" : playerBuffer[bufferPosition].mediaPlayer().media().meta().get(Meta.ALBUM);
	}

	@Override
	public int getVolume() {
		return playerBuffer[bufferPosition].mediaPlayer().audio().volume();
	}

	@Override
	public void setPositionUpdatedAction(Runnable r) {
		finishedAction = r;
	}

	@Override
	public void setPosition(float position) {
		playerBuffer[bufferPosition].mediaPlayer().controls().setPosition(position);
	}

	@Override
	public float getPosition() {
		return playerBuffer[bufferPosition].mediaPlayer().status().position();
	}

	@Override
	public long getDuration() {
		return playerBuffer[bufferPosition].mediaPlayer().media().info().duration();
	}

}
