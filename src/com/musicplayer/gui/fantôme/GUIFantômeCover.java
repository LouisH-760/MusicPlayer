package com.musicplayer.gui.fantôme;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.musicplayer.gui.GUISwingPanel;

/**
 * Cover panel with support to add buttons
 * @author Vincent Carpentier
 *
 */
public class GUIFantômeCover extends GUISwingPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private ArrayList<ButtonFantôme> buttonFantômes = new ArrayList<>();

	/**
	 * Listen is own mouse event
	 */
	public GUIFantômeCover() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/* (non-Javadoc)
	 * @see com.musicplayer.gui.GUISwingPanel#paintComponent(java.awt.Graphics)
	 * add buttons render
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (ButtonFantôme buttonFantôme : buttonFantômes)
			buttonFantôme.render(g);
	}
	
	/**
	 * @param buttonFantôme cannot be null
	 */
	public void addButtonFantôme(ButtonFantôme buttonFantôme) {
		if (buttonFantôme != null)
			buttonFantômes.add(buttonFantôme);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (ButtonFantôme buttonFantôme : buttonFantômes)
			if (buttonFantôme.isIn(e.getX(), e.getY()))
				buttonFantôme.doClick();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		updateMouse(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//TODO more opérations
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updateMouse(e);
	}
	
	/**
	 * Private method to do the same actions in different mouse event
	 * @param e
	 */
	private void updateMouse (MouseEvent e) {
		for (ButtonFantôme buttonFantôme : buttonFantômes)
			buttonFantôme.setHover(buttonFantôme.isIn(e.getX(), e.getY()));
		repaint();
	}

}
