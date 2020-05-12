package com.musicplayer.logic;

import java.net.URISyntaxException;

import com.musicplayer.gui.GUI;
import com.musicplayer.gui.swing.SwingGUI;
import com.musicplayer.media.Album;
import com.musicplayer.media.Song;
import com.musicplayer.player.Player;
import com.musicplayer.player.vlcjsingle.VLCJSinglePlayer;
import com.musicplayer.scanner.Scanner;
import com.musicplayer.scanner.SingleFolderScanner;

public class SingleAlbumMediaLogic implements Logic {

	private final Runnable updateTitleLabel;
	
	private static final String DEFAULT_IMAGE = "default.png";
	
	private Scanner mediaScanner;
	private GUI gui;
	private Player player;
	private Album album;
	
	public SingleAlbumMediaLogic(String[] args) throws URISyntaxException {
		if (args.length < 1) {
			System.err.println("This program requires a path argument to play!");
			System.exit(-1);
		} else if (args.length > 1) {
			System.out.println("Warning: Only the first argument is used.");
		}
		mediaScanner = new SingleFolderScanner(args[0]);
		// using the current scanner, there is one album given back anyways
		// otherwise, only play the first
		album = mediaScanner.getAlbums().get(0);
		gui = new SwingGUI(album.getName());
		player = new VLCJSinglePlayer();
		updateTitleLabel = () -> {
			String withArtist = player.nowPlayingArtist() + " - " + player.nowPlayingTitle(); 	// test label to check for
			// width
			if (!gui.isStringTooWide(withArtist)) // if it is narrow enough to fit on the screen
				gui.setTrackLabel(withArtist); // display title and artist (artist - title)
			else
				gui.setTrackLabel(player.nowPlayingTitle()); // else, only display the title
			if(album.getCoverArtUri() != null)
				gui.setAlbumArt(album.getCoverArtUri());
			else
				gui.setAlbumArt(DEFAULT_IMAGE);
		};
		
		prepare();
	}
	
	@Override
	public void start() {
		gui.showWindow();
		player.play();
	}
	
	private void prepare() {
		if(album.getCoverArtUri() != null)
			gui.setAlbumArt(album.getCoverArtUri());
		else
			gui.setAlbumArt(DEFAULT_IMAGE);
		gui.setTrackLabel(album.getName());
		gui.setWindowName(album.getName());
		setRunnables();
		addSongs();
	}
	
	private void setRunnables() {
		gui.setNextAction(() -> player.next());
		gui.setPauseAction(() -> {
				if (player.isPaused()) // if the player is paused
					player.play(); // then play
				else
					player.pause(); // else, pause playback
			});
		gui.setPreviousAction(() -> player.previous());
		gui.setVUpAction(() -> {
			System.out.println("New volume: " + player.getVolume());
			player.volumeUp();
		});
		gui.setVDownAction(() -> {
			System.out.println("New volume: " + player.getVolume());
			player.volumeDown();
		});
		player.setPositionUpdatedAction(() -> gui.setSeekbarPosition(player.getPosition()));
		gui.setSeekbarMovedAction(() -> player.setPosition(gui.getSeekPosition()));
		player.setUpdateMediaAction(updateTitleLabel);
		gui.setGainedFocusAction(updateTitleLabel);
	}
	
	private void addSongs() {
		for(Song song : album.getSongs()) {
			player.add(song.getLocation());
		}
	}

}
