package com.musicplayer.gui.swing;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Seekbar component for the Swing GUI
 * @author louis Hermier
 *
 */
public class Seekbar extends JSlider{
	private static final long serialVersionUID = 5L;
	private final int MAX = 100000;
	private final int MIN = 0;
	private Runnable positionChangedAction;
	private boolean adjusted;
	private float newPos;
	
	public Seekbar() {
		setMinimum(MIN);
		setMaximum(MAX);
		setValue(MIN);
		setPaintTicks(false);
		setPaintLabels(false);
		adjusted = false;
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				JSlider source = (JSlider) arg0.getSource();
				if (source.getValueIsAdjusting()) {
					adjusted = true;
					newPos = source.getValue();
				} else if (adjusted && !source.getValueIsAdjusting() && positionChangedAction != null) {
					positionChangedAction.run();
					adjusted = false;
				}
			}
			
		});
	}
	
	/**
	 * Set the displayed seekbar's position
	 * @param position > float between 0 (beginning) and 1 (end)
	 */
	public void setPosition(float position) {
		int redonePosition = (int)(position * MAX);
		if(!adjusted) {
			setValue(redonePosition);
		}
	}
	
	/**
	 * get the sekbar's position
	 * @return position > float between 0 (beginning) and 1 (end)
	 */
	public float getPosition() {
		return (float)getPosition() / (float)MAX;
	}
	
	/**
	 * get the seeked position
	 * @return position > float between 0 (beginning) and 1 (end)
	 */
	public float getNewPosition() {
		return newPos / (float)MAX;
	}
	
	/**
	 * Set what happens when the user changes the position
	 * @param r
	 */
	public void setPositionChanged(Runnable r) {
		positionChangedAction = r;
	}
}
