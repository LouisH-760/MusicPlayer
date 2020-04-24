package com.musicplayer.scanner;

import java.util.ArrayList;

/**
 * Defines the minimum framework to implement a scanner to be a drop-in replacement in Main.
 * @author louis Hermier
 * See folderScanner for an example of implementation
 */
public interface Scanner {
	// get the cover art to display
	public ArrayList<String> getCoverArt();
	// get all the music files to play
	public ArrayList<String> getAllMusicFiles();
	// get all the images associated with the content
	public ArrayList<String> getAllImageFiles();
	// get the name of the album that will be played
	// (is ideally then replaced by the album gotten from the player)
	public String getAlbumName();
}
