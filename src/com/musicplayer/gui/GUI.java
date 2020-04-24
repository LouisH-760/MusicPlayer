package com.musicplayer.gui;


/**
 * Defines the minimum framework to implement a player to be a drop-in replacement in Main.
 * @author louis Hermier
 * See SwingGUI for an example implementation
 */
public interface GUI {
	// make the window visible to the user
	public void showWindow();
	// set the filename to the image to display in the album art field
	public void setAlbumArt(String filename);
	// set the information to display about the track
	public void setTrackLabel(String label);
	// set the name of the window
	public void setWindowName(String name);
	// check if a string is too wide to display properly
	public boolean isStringTooWide(String str);
	// these are used to bind the GUI with the Player
	// This way, the GUI and the Player are completely agnostic from one another
	// set what runs when the "next" button is clicked
	public void setNextAction(Runnable r);
	// set what runs when the "previous" button is clicked
	public void setPreviousAction(Runnable r);
	// set what runs when the "play/pause" button is clicked
	public void setPauseAction(Runnable r);
	// set what runs when the "volume up" button is clicked
	public void setVUpAction(Runnable r);
	// set what runs when the "volume down" button is clicked
	public void setVDownAction(Runnable r);
}