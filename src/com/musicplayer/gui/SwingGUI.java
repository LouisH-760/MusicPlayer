package com.musicplayer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.musicplayer.keyboard.ShortcutBinding;

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
	private JPanel controls;
	private Button next;
	private Button pause;
	private Button previous;
	private Button vUp;
	private Button vDown;
	private JLabel trackLabel;
	private Seekbar seekbar;
	
	private ShortcutBinding sb;

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
		container = (JPanel) getContentPane();//No need to build a new JPanel
		seekbar = new Seekbar();
		navigation = new JPanel();
		controls = new JPanel();
		trackLabel = new JLabel("Empty");
		panel = new GUISwingPanel();
		trackInfo = new JPanel();
		
		sb = new ShortcutBinding(this);
		
		//To avoid focus and space key not working shortcut
		next.setFocusable(false);
		pause.setFocusable(false);
		previous.setFocusable(false);
		vUp.setFocusable(false);
		vDown.setFocusable(false);
		
		setFocusable(true);
		setTitle(title);
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		container.setLayout(new BorderLayout());
		container.add(panel, BorderLayout.CENTER);
		controls.setLayout(new BorderLayout());
		navigation.setLayout(new FlowLayout());
		navigation.add(vDown);
		navigation.add(previous);
		navigation.add(pause);
		navigation.add(next);
		navigation.add(vUp);
		controls.add(navigation, BorderLayout.CENTER);
		controls.add(seekbar, BorderLayout.NORTH);
		trackInfo.add(trackLabel);
		container.add(trackInfo, BorderLayout.NORTH);
		container.add(controls, BorderLayout.SOUTH);
		
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
		next.setClicked(r);
		sb.addBind(KeyEvent.VK_RIGHT, 0, r);
	}
	
	/**
	 * select what happens when the "previous" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setPreviousAction(Runnable r) {
		previous.setClicked(r);
		sb.addBind(KeyEvent.VK_LEFT, 0, r);
	}

	/**
	 * select what happens when the "pause" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setPauseAction(Runnable r) {
		pause.setClicked(r);
		sb.addBind(KeyEvent.VK_SPACE, 0, r);
	}
	
	/**
	 * select what happens when the "volume up" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setVUpAction(Runnable r) {
		vUp.setClicked(r);
		sb.addBind(KeyEvent.VK_UP, 0, r);
	}
	
	/**
	 * select what happens when the "volume down" button is clicked
	 * (for example, lambda with player.next())
	 * @param Runnable
	 */
	public void setVDownAction(Runnable r) {
		vDown.setClicked(r);
		sb.addBind(KeyEvent.VK_DOWN, 0, r);
	}

	@Override
	public void setSeekbarPosition(float position) {
		seekbar.setPosition(position);
	}

	@Override
	public float getSeekbarPosition() {
		return seekbar.getPosition();
	}

	@Override
	public void setSeekbarMovedAction(Runnable r) {
		seekbar.setPositionChanged(r);
	}

}
