package com.musicplayer.gui;

import java.net.URI;

/**
 * Defines the minimum framework to implement a player to be a drop-in replacement in Main.
 * @author louis Hermier
 * See SwingGUI for an example implementation
 */
public interface GUI {
	/**
	 * make the window visible to the user
	 */
	public void showWindow();
	/**
	 *  set the filename to the image to display in the album art field
	 * @param filename : path of the file to display
	 */
	public void setAlbumArt(String filename);
	/**
	 *  set the album art with an URI
	 * @param uri : URI of the album art to display
	 */
	public void setAlbumArt(URI uri);
	/**
	 *  set the information to display about the track
	 * @param label : all the informatoin
	 */
	public void setTrackLabel(String label);
	/**
	 *  set the name of the window
	 * @param name : name of the window
	 */
	public void setWindowName(String name);
	/**
	 *  check if a string is too wide to display properly
	 * @param str : string to check for width
	 * @return true if the string fits, false otherwise
	 */
	public boolean isStringTooWide(String str);
	 /**
	  * these are used to bind the GUI with the Player
	  * This way, the GUI and the Player are completely agnostic from one another
	  * set what runs when the "next" action is triggered
	  */
	public void setNextAction(Runnable r);
	/**
	 *  set what runs when the "previous" action is triggered
	 * @param r : what to execute
	 */
	public void setPreviousAction(Runnable r);
	/**
	 *  set what runs when the "play/pause" action is triggered
	 * @param r : what to execute
	 */
	public void setPauseAction(Runnable r);
	/**
	 *  set what runs when the "volume up" action is triggered
	 * @param r : what to execute
	 */
	public void setVUpAction(Runnable r);
	/**
	 *  set what runs when the "volume down" action is triggered
	 * @param r : what to execute
	 */
	public void setVDownAction(Runnable r);
	/**
	 *  set the seekbar position
	 * @param position : new position of the seekbar
	 */
	public void setSeekbarPosition(float position);
	/**
	 *  get the seekbar position
	 * @return position of the seekbar
	 */
	public float getSeekbarPosition();
	/**
	 *  what happens when the seekbar is moved by the user
	 * @param r : what to execute
	 */
	public void setSeekbarMovedAction(Runnable r);
	/**
	 *  get the seeking position
	 * @return seeking position, ie where the user moved the seekbar
	 */
	public float getSeekPosition();
	/**
	 *  set what to run when the window regains focus
	 *  useful to update a label that only gets updated once per song, for example
	 * @param r : what to execute
	 */
	public void setGainedFocusAction(Runnable r);
}
