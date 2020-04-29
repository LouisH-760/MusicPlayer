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
	private static final CoverDisplayType COVER_DISPLAY_TYPE = CoverDisplayType.SEE_WHOLE_COVER;

	private String filename;
	private Image image;

	/**
	 * Rendering part
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// rendering hints: antialiasing on, bicubic interpolation for images
		// doesn't seem to really help?
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.addRenderingHints(
				new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
		// if the filename or image wasn't set yet, can't render anything
		if (filename != null && image != null) {
			int w = image.getWidth(null); // get the width of the image
			int h = image.getHeight(null); // get the height of the image
			// if the image is higher than it is tall
			// eg tape cover, only front and not whole J-card
			float ratioHeight = (float) getHeight() / h;
			float ratioWidth = (float) getWidth() / w;
			float ratio;

			switch (COVER_DISPLAY_TYPE) {
			case FILL_DISPLAY:
				ratio = Math.max(ratioWidth, ratioHeight);// Looking for the maximum ratio of width or height to fill
															// the panel
				break;
			case SEE_WHOLE_COVER:
				ratio = Math.min(ratioWidth, ratioHeight);// Looking for the minimum ratio of width or height to fit
															// into the panel
				break;
			default:
				throw new IllegalArgumentException("Wrong cover display parameter");
			}

			int newHeight = (int) (h * ratio);
			int newWidth = (int) (w * ratio);
			// Setting marges
			g.drawImage(image, (getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2, newWidth, newHeight, this);
			// image is centered and rezise to match width or height of the panel
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
		try {
			File file = new File(filename);
			if(file.canRead()) {
				image = ImageIO.read(file);// get the image
			}
				
		} catch (IOException e) {
			System.out.println("Error (" + e.toString() + ") while loading the picture (" + this.filename + ")");
			e.printStackTrace();
		}
	}
}