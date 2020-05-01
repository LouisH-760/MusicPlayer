package com.musicplayer.scanner;

import java.util.List;

/**
 * Defines the minimum framework to implement a scanner to be a drop-in replacement in Main.
 * @author louis Hermier
 * See folderScanner for an example of implementation
 */
public interface Scanner {
	/**
	 *  get the cover art to display
	 * @return a list of the images that match the "cover art" criteria (ie named Cover.ext, ...)
	 */
	public List<String> getCoverArt();
	/**
	 *  get all the music files to play
	 * @return list of all the music files
	 */
	public List<String> getAllMusicFiles();
	/**
	 *  get all the images associated with the content
	 * @return list of all the images
	 */
	public List<String> getAllImageFiles();
	/**
	 *  get the name of the album that will be played
	 *  (is ideally then replaced by the album gotten from the player)
	 * @return : name of the album (can be inferred from the folder name, ...)
	 */
	public String getAlbumName();
}
