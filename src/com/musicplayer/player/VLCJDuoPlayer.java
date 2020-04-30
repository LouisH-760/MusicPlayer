package com.musicplayer.player;

import java.util.ArrayList;
import java.util.List;

import com.musicplayer.misc.Helper;

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class VLCJDuoPlayer implements Player {

	private List<String> songs = new ArrayList<String>();
	private AudioPlayerComponent[] playerBuffer = new AudioPlayerComponent[2];
	private AudioPlayerComponent tampon;
	private VLCJMediaPlayerEventAdapter vlcjDuoMediaPlayerEventAdapter;
	private int songsPosition = 0;
	private Runnable updateMediaAction;
	private int incr = 5;

	public VLCJDuoPlayer() {
		vlcjDuoMediaPlayerEventAdapter = new VLCJMediaPlayerEventAdapter();
		vlcjDuoMediaPlayerEventAdapter.setFinishedAction(() -> next());
		
		 MediaEventAdapter mediaEventAdapter = new MediaEventAdapter() {
			public void mediaParsedChanged(Media media, MediaParsedStatus status) {
				 if(status == MediaParsedStatus.DONE) { 
					 // if it succeeded
					 updateMediaAction.run();
				 }	
			 }
		};
		
		for (int i = 0; i < playerBuffer.length; i++) {
			playerBuffer[i] = new AudioPlayerComponent();
			playerBuffer[i].mediaPlayer().events().addMediaPlayerEventListener(vlcjDuoMediaPlayerEventAdapter);
			playerBuffer[i].mediaPlayer().events().addMediaEventListener(mediaEventAdapter);
		}
	}

	private boolean swapSong() {
		return swapSong(false);
	}

	private boolean swapSong(boolean reverse) {
		if (!reverse) {
			if (songsPosition + 1 < songs.size()) {
				playerBuffer[1].mediaPlayer().controls().play();
				playerBuffer[0].mediaPlayer().controls().stop();
				swapBuffer();
				if (++songsPosition + 1 < songs.size())
					loadSong(songsPosition + 1, 1);
				return true;
			}
		} else {
			if (songsPosition > 0) {
				playerBuffer[0].mediaPlayer().controls().stop();
				swapBuffer();
				loadSong(--songsPosition, 0);
				playerBuffer[0].mediaPlayer().controls().play();
				return true;
			}
		}
		return false;
	}

	private void swapBuffer() {
		tampon = playerBuffer[0];
		playerBuffer[0] = playerBuffer[1];
		playerBuffer[1] = tampon;
	}

	private void loadSong(int songPosition, int bufferPosition) {
		playerBuffer[bufferPosition].mediaPlayer().media().prepare(songs.get(songPosition));
		playerBuffer[bufferPosition].mediaPlayer().media().parsing().parse();
	}

	@Override
	public void add(String song) {
		boolean first = songs.size() == 0;
		boolean end = (songs.size() >= songsPosition - 1);
		songs.add(song);
		if (first)
			loadSong(songsPosition, 0);
		if (end)
			loadSong(songsPosition + 1, 1);
	}

	@Override
	public void addMultiple(List<String> songs) {
		boolean first = this.songs.size() == 0;
		boolean end = (this.songs.size() >= songsPosition - 1) && (songs.size() > 0);
		this.songs.addAll(songs);
		if (first)
			loadSong(songsPosition, 0);
		if (end)
			loadSong(songsPosition + 1, 1);
	}

	@Override
	public void play() {
		playerBuffer[0].mediaPlayer().controls().play();
	}

	@Override
	public void pause() {
		playerBuffer[0].mediaPlayer().controls().pause();
	}

	@Override
	public void next() {
		if (!swapSong())
			System.out.println("End list reach !");
		updateMediaAction.run();
	}

	@Override
	public void previous() {
		if (!swapSong(true))
			System.out.println("Start list reach !");
		updateMediaAction.run();
	}

	@Override
	public void stop() {
		playerBuffer[0].mediaPlayer().controls().stop();
	}

	@Override
	public void setVolume(int volume) {
		Helper.check(volume <= MAX_VOLUME, "You cannot set the volume this high");
		Helper.check(volume >= MIN_VOLUME, "You cannot set the volume this low");
		playerBuffer[0].mediaPlayer().audio().setVolume(volume);
		playerBuffer[1].mediaPlayer().audio().setVolume(volume);
	}

	@Override
	public boolean isPaused() {
		return !playerBuffer[0].mediaPlayer().status().isPlaying();
	}

	@Override
	public void volumeDown() {
		if (getVolume() >= MIN_VOLUME + incr)
			setVolume(getVolume() - incr);
	}

	@Override
	public void volumeUp() {
		if (getVolume() <= MAX_VOLUME - incr)
			setVolume(getVolume() + incr);
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
		updateMediaAction = r;
	}

	@Override
	public String nowPlayingTitle() {
		return (playerBuffer[0].mediaPlayer().media().meta() == null) ? ""
				: playerBuffer[0].mediaPlayer().media().meta().get(Meta.TITLE);
	}

	@Override
	public String nowPlayingArtist() {
		return (playerBuffer[0].mediaPlayer().media().meta() == null) ? ""
				: playerBuffer[0].mediaPlayer().media().meta().get(Meta.ARTIST);
	}

	@Override
	public String nowPlayingAlbum() {
		return (playerBuffer[0].mediaPlayer().media().meta() == null) ? ""
				: playerBuffer[0].mediaPlayer().media().meta().get(Meta.ALBUM);
	}

	@Override
	public int getVolume() {
		return playerBuffer[0].mediaPlayer().audio().volume();
	}

	@Override
	public void setPositionUpdatedAction(Runnable r) {
		vlcjDuoMediaPlayerEventAdapter.setPositionUpdatedAction(r);
	}

	@Override
	public void setPosition(float position) {
		playerBuffer[0].mediaPlayer().controls().setPosition(position);
	}

	@Override
	public float getPosition() {
		return playerBuffer[0].mediaPlayer().status().position();
	}

	@Override
	public long getDuration() {
		return playerBuffer[0].mediaPlayer().media().info().duration();
	}

}
