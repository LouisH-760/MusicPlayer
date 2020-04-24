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
 * @author louis Hermier
 *
 */
public class GUISwingPanel extends JPanel{
	private static final long serialVersionUID = 2L;
	
	private String filename;
	private int imageH;
	private int imageW;
	private int imageOffset;

	/**
	 * Rendering part
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		// rendering hints: antialiasing on, bicubic interpolation for images
		// doesn't seem to really help?
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
		// if the filename wasn't set yet, can't render anything
		if(filename != null) {
	    	try {
	    	      Image img = ImageIO.read(new File(filename)); // get the image
	    	      float w = img.getWidth(null); // get the width of the image
	    	      float h = img.getHeight(null); // get the height of the image
	    	      float ratio = h / w; // get the aspect ratio of the image
	    	      // if the image is higher than it is tall
	    	      // eg tape cover, only front and not whole J-card
	    	      if(ratio > 1.0) {
	    	    	  ratio = w / h;
	    	    	  imageH = this.getWidth();
	    	    	  imageW = (int) (this.getWidth() * ratio);
	    	    	  imageOffset = (imageH - imageW) / 2;
	    	    	  g.drawImage(img,  imageOffset,  0, imageW, imageH, this);
	    	      }
	    	      else {
	    	    	// allows non-square imaged to not appear stretched while fitting the whole width of the window
		    	    imageW = this.getWidth(); // the image will take the width of the panel
		    	    imageH = (int) (this.getWidth() * ratio); // the height of the image is not necesarrily the same
		    	    // as not all images are square
		    	    // think digipak cover.
		    	    imageOffset = (imageW - imageH) / 2;
		    	    // calculation for how much blank space to put above the image for it to be vertically centerd
		    	    g.drawImage(img, 0, imageOffset, imageW, imageH, this); // fit image to window width while preserving aspect ratio
		    	    // image is centered if it is not as tall as it is wide (eg digipak covers)
	    	      }
	    	    } catch (IOException e) {
	    	      System.out.println("Error (" + e.toString() + ") while loading the picture");
	    	    } 
	    		catch (Exception e) {
	    			System.out.println("Error (" + e.toString() + ") while loading the picture");
	    		}
	    }
	}
	
	/**
	 * Set the filename of the image to render as cover art
	 * @param filename > full path
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
