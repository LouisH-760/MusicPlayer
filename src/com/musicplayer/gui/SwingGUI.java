package com.musicplayer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create a Swing GUI for the player
 * @author louis Hermier
 *
 */
public class SwingGUI  extends JFrame implements GUI{

	private static final long serialVersionUID = 1L;
	
	private GUISwingPanel panel;
	private JPanel container;
	private JPanel navigation;
	private JPanel trackInfo;
	private Button next;
	private Button pause;
	private Button previous;
	private Button vUp;
	private Button vDown;
	private JLabel trackLabel;
	
	private final int WIDTH = 275;
	private final int HEIGHT = WIDTH + 75;
	
	/**
	 * Initialize the window
	 * @param title : title of the window
	 * recommended: the album name
	 */
	public SwingGUI(String title) {
		next = new Button("►");
		pause = new Button("⏯️");
		previous = new Button("◄");
		vUp = new Button("🔊");
		vDown = new Button("🔉");
		container = new JPanel();
		navigation = new JPanel();
		trackLabel = new JLabel("Empty");
		panel = new GUISwingPanel();
		trackInfo = new JPanel();
		
		this.setTitle(title);
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		container.setLayout(new BorderLayout());
		container.add(panel, BorderLayout.CENTER);
		navigation.setLayout(new FlowLayout());
		navigation.add(vDown);
		navigation.add(previous);
		navigation.add(pause);
		navigation.add(next);
		navigation.add(vUp);
		trackInfo.add(trackLabel);
		container.add(trackInfo, BorderLayout.NORTH);
		container.add(navigation, BorderLayout.SOUTH);
		this.setContentPane(container);
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
		this.setVisible(true);
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
		next.setClicked(r);
	}
	
	/**
	 * select what happens when the "previous" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setPreviousAction(Runnable r) {
		previous.setClicked(r);
	}

	/**
	 * select what happens when the "pause" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setPauseAction(Runnable r) {
		pause.setClicked(r);
	}
	
	/**
	 * select what happens when the "volume up" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setVUpAction(Runnable r) {
		vUp.setClicked(r);
	}
	
	/**
	 * select what happens when the "volume down" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setVDownAction(Runnable r) {
		vDown.setClicked(r);
	}

}
