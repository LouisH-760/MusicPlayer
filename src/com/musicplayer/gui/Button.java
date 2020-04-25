package com.musicplayer.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * Simple button for the swing gui
 * @author louis hermier
 *
 */
public class Button extends JButton implements MouseListener{
	private static final long serialVersionUID = 3L;

	private Runnable action;
  
	/**
	 * create the button, and add a mouse listener to it
	 * @param str > label of the button
	 */
	public Button(String str){
		super(str);
		addMouseListener(this);
	}

	/**
	 * Select the action to be run when the button is clicked
	 * @param r
	 */
	public void setClicked(Runnable r) {
		action = r;
	}
	//Méthode appelée lors du clic de souris
	/**
	 * What to run when the mouse is clicked: the runnable set earlier with setClicked()
	 * shouldn't be null ig
	 */
	public void mouseClicked(MouseEvent event) { 
		action.run();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// do nothing
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// do nothing
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// do nothing
	}     
}