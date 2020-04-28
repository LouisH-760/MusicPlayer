package com.musicplayer.gui.fantôme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.musicplayer.gui.GUI;
import com.musicplayer.keyboard.ShortcutBinding;

/**
 * Complete GUI for the player
 * @author Vincent Carpentier
 *
 */
public class GUIFantôme extends JFrame implements GUI{

	private static final long serialVersionUID = 1L;
	
	private GUIFantômeCover panel;
	private JPanel container;
	private JPanel trackInfo;
	private ButtonFantôme next;
	private ButtonFantôme pause;
	private ButtonFantôme previous;
	private ButtonFantôme vUp;
	private ButtonFantôme vDown;
	private JLabel trackLabel;
	
	private ShortcutBinding sb;

	private final int WIDTH = 275;
	private final int HEIGHT = WIDTH + 75;
	
	/**
	 * Initialize the window
	 * @param title : title of the window
	 * recommended: the album name
	 */
	public GUIFantôme(String title) {
		container = (JPanel) getContentPane();//No need to build a new JPanel
		trackLabel = new JLabel("Empty");
		panel = new GUIFantômeCover();
		trackInfo = new JPanel();
		
		sb = new ShortcutBinding(this);
		
		setFocusable(true);
		setTitle(title);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		container.setLayout(new BorderLayout());
		container.add(panel, BorderLayout.CENTER);
		trackInfo.add(trackLabel);
		container.add(trackInfo, BorderLayout.NORTH);

		pack();//To put all component size.
		
		//Temporary button sizes and color
		Color color = new Color(0,0,0,190);
		next = new ButtonFantôme(2*panel.getWidth()/3, 0, panel.getWidth()/3, panel.getHeight(), color, color);
		pause = new ButtonFantôme(panel.getWidth()/3, panel.getHeight()/3, panel.getWidth()/3, panel.getHeight()/3, color, color);
		previous = new ButtonFantôme(0, 0, panel.getWidth()/3, panel.getHeight(), color, color);
		vUp = new ButtonFantôme(panel.getWidth()/3, 0, panel.getWidth()/3, panel.getHeight()/3, color, color);
		vDown = new ButtonFantôme(panel.getWidth()/3, 2*panel.getHeight()/3, panel.getWidth()/3, panel.getHeight()/3, color, color);
		
		panel.addButtonFantôme(next);
		panel.addButtonFantôme(pause);
		panel.addButtonFantôme(previous);
		panel.addButtonFantôme(vUp);
		panel.addButtonFantôme(vDown);
	}
	
	/**
	 * Set the text to display for track information (above album art)
	 */
	public void setTrackLabel(String label) {
		trackLabel.setText(label);
	}
	
	/**
	 * set the title of the window
	 */
	public void setWindowName(String name) {
		this.setTitle(name);
	}
	
	/**
	 * check if a string is too wide to fit wholly into the window
	 */
	public boolean isStringTooWide(String str) {
		JLabel label = new JLabel(str);
		label.setSize(label.getPreferredSize());
		int potentialWidth = label.getWidth();
		int maxWidth = WIDTH;
		return potentialWidth > maxWidth;
	}
	
	/**
	 * Show the window
	 */
	public void showWindow() {
		setVisible(true);
	}
	
	/**
	 * Give the path of the image to be displayed as cover
	 * @param path to the image
	 */
	public void setAlbumArt(String filename) {
		panel.setFilename(filename);
	}
	
	/**
	 * select what happens when the "next" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setNextAction(Runnable r) {
		next.setAction(r);
		sb.addBind(KeyEvent.VK_RIGHT, 0, r);
	}
	
	/**
	 * select what happens when the "previous" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setPreviousAction(Runnable r) {
		previous.setAction(r);
		sb.addBind(KeyEvent.VK_LEFT, 0, r);
	}

	/**
	 * select what happens when the "pause" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setPauseAction(Runnable r) {
		pause.setAction(r);
		sb.addBind(KeyEvent.VK_SPACE, 0, r);
	}
	
	/**
	 * select what happens when the "volume up" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setVUpAction(Runnable r) {
		vUp.setAction(r);
		sb.addBind(KeyEvent.VK_UP, 0, r);
	}
	
	/**
	 * select what happens when the "volume down" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setVDownAction(Runnable r) {
		vDown.setAction(r);
		sb.addBind(KeyEvent.VK_DOWN, 0, r);
	}

}
