package com.musicplayer.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * JPanel responsible for the scaled display of the cover art
 * 
 * @author louis Hermier
 *
 */
public class GUISwingPanel extends JPanel {
	private static final long serialVersionUID = 2L;

	private String filename;

	/**
	 * Rendering part
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// rendering hints: antialiasing on, bicubic interpolation for images
		// doesn't seem to really help?
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
		// if the filename wasn't set yet, can't render anything
		if (filename != null) {
			try {
				Image img = ImageIO.read(new File(filename)); // get the image
				int w = img.getWidth(null); // get the width of the image
				int h = img.getHeight(null); // get the height of the image
				// if the image is higher than it is tall
				// eg tape cover, only front and not whole J-card
				float ratioHeight = (float)getHeight() / h;
				float ratioWidth = (float)getWidth() / w;
				
				//Looking for the minimum ratio of width or height to fit into the panel
				if (ratioHeight > ratioWidth) {//When scaling up, the width go out of the panel width first
					int newHeight = (int) (h*ratioWidth);
					g.drawImage(img, 0, (getHeight() - newHeight)/2, (int)(w*ratioWidth), newHeight, this);
				} else {//When scaling up, the height go out of the panel height first
					int newWidth = (int) (w*ratioHeight);
					g.drawImage(img, (getWidth() - newWidth)/2, 0, newWidth, (int)(h*ratioHeight), this);
				}
				// image is centered and rezise to match width or height of the panel
			} catch (Exception e) {
				System.out.println("Error (" + e.toString() + ") while loading the picture");
			}
		}
	}

	/**
	 * Set the filename of the image to render as cover art
	 * 
	 * @param filename
	 *            > full path
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
