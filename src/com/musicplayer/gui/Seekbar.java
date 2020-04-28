package com.musicplayer.gui;

import javax.swing.JSlider;

public class Seekbar extends JSlider{
	public final int MAX = 100000;
	public final int MIN = 0;
	
	public Seekbar() {
		setMinimum(MIN);
		setMaximum(MAX);
		setValue(MIN);
		setPaintTicks(false);
		setPaintLabels(false);
	}
	
	public void setPosition(float position) {
		int redonePosition = (int)(position * MAX);
		setValue(redonePosition);
	}
}
