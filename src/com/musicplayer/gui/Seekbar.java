package com.musicplayer.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Seekbar extends JSlider{
	private static final long serialVersionUID = 5L;
	private final int MAX = 100000;
	private final int MIN = 0;
	private Runnable positionChangedAction;
	
	public Seekbar() {
		setMinimum(MIN);
		setMaximum(MAX);
		setValue(MIN);
		setPaintTicks(false);
		setPaintLabels(false);
		addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				JSlider source = (JSlider) arg0.getSource();
				if (source.getValueIsAdjusting() && positionChangedAction != null) {
					positionChangedAction.run();
				}
			}
			
		});
	}
	
	public void setPosition(float position) {
		int redonePosition = (int)(position * MAX);
		setValue(redonePosition);
	}
	
	public float getPosition() {
		float redonePosition = (float)getPosition() / (float)MAX;
		return redonePosition;
	}
	
	public void setPositionChanged(Runnable r) {
		positionChangedAction = r;
	}
}
