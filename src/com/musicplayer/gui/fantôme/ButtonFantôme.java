package com.musicplayer.gui.fantôme;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * Basic button adapted to the GUIFantôme usages
 * @author Vincent Carpentier
 *
 */
public class ButtonFantôme {

	private int x;
	private int y;
	private int width;
	private int height;

	private Color hoverBackgroungColor = Color.WHITE;
	/*
	 * @Depredated Useless at the moment
	 */
	@Deprecated
	private Color pressedBackgroungColor = Color.WHITE;

	private Icon hoverIcon = null;
	/*
	 * @Depredated Useless at the moment
	 */
	@Deprecated
	private Icon pressedIcon = null;

	private boolean hover = false;
	
	protected Runnable action;

	/**
	 * @param x horizontal position
	 * @param y vertical position
	 * @param width horizontal size
	 * @param height vertical size
	 */
	public ButtonFantôme(int x, int y, int width, int height) {
		this(x, y, width, height, null, null);
	}

	/**
	 * @param x horizontal position
	 * @param y vertical position
	 * @param width horizontal size
	 * @param height vertical size
	 * @param hoverBackgroungColor color render when the button is hover
	 * @param pressedBackgroungColor currently useless
	 * @param hoverIcon icon render when the button is hover
	 */
	public ButtonFantôme(int x, int y, int width, int height, Color hoverBackgroungColor,
			Color pressedBackgroungColor) {
		this(x, y, width, height, hoverBackgroungColor, pressedBackgroungColor, null, null);
	}

	/**
	 * @param x horizontal position
	 * @param y vertical position
	 * @param width horizontal size
	 * @param height vertical size
	 * @param hoverBackgroungColor color render when the button is hover
	 * @param pressedBackgroungColor currently useless
	 * @param hoverIcon icon render when the button is hover
	 * @param pressedIcon currently useless
	 */
	public ButtonFantôme(int x, int y, int width, int height, Color hoverBackgroungColor,
			Color pressedBackgroungColor, Icon hoverIcon, Icon pressedIcon) {
		this(x, y, width, height, hoverBackgroungColor, pressedBackgroungColor, hoverIcon, pressedIcon, null);
	}
	
	/**
	 * @param x horizontal position
	 * @param y vertical position
	 * @param width horizontal size
	 * @param height vertical size
	 * @param hoverBackgroungColor color render when the button is hover
	 * @param pressedBackgroungColor currently useless
	 * @param hoverIcon icon render when the button is hover
	 * @param pressedIcon currently useless
	 * @param action the action to do when button is called to resolve its action
	 */
	public ButtonFantôme(int x, int y, int width, int height, Color hoverBackgroungColor,
			Color pressedBackgroungColor, Icon hoverIcon, Icon pressedIcon, Runnable action) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setHoverBackgroungColor(hoverBackgroungColor);
		this.pressedBackgroungColor = pressedBackgroungColor;
		this.hoverIcon = hoverIcon;
		this.pressedIcon = pressedIcon;
		this.action = action;
	}
	
	/**
	 * Method to render button
	 * @param g
	 */
	public void render (Graphics g) {
		g.setColor(hoverBackgroungColor);
		if (hover) {
			g.fillRect(x, y, width + 1, height + 1);
		}
	}
	
	/**
	 * @param action set the action when the button is called to do the action
	 */
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	/**
	 * Do button action
	 */
	public void doClick() {
		if (action != null)
			action.run();
	}

	/**
	 * @param a horizontal coord of the point
	 * @param b vertical coord of the point
	 * @return true if the point is in the button, else false
	 */
	public boolean isIn(int a, int b) {
		return (collisionPoint1D(a, x, width) && collisionPoint1D(b, y, height));
	}

	/**
	 * @param a point to test if in the segment
	 * @param x start point of the segment
	 * @param w width of the segment
	 * @return true if a is in the segment, else false
	 */
	private boolean collisionPoint1D(int a, int x, int w) {
		return (a >= x && a <= x + w);
	}

	/**
	 * Return the horizontal position
	 * @return x an integer represente relative position to the parent
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return the vertical position
	 * @return y an integer represente relative position to the parent
	 */
	public int getY() {
		return y;
	}

	/**
	 * Return the width of the component
	 * @return width an integer
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Return the height of the component
	 * @return height an integer
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return Color, the background color when button is hover
	 */
	public Color gethoverBackgroungColor() {
		return hoverBackgroungColor;
	}

	/**
	 * @param hoverBackgroungColor must be not null
	 */
	public void setHoverBackgroungColor(Color hoverBackgroungColor) {
		if (hoverBackgroungColor != null)
			this.hoverBackgroungColor = hoverBackgroungColor;
		else 
			throw new IllegalArgumentException("Default Backgroud color cannot be null !");
	}

	/**
	 * @deprecated no current use
	 * @return Color
	 */
	@Deprecated
	public Color getPressedBackgroungColor() {
		return pressedBackgroungColor;
	}

	/**
	 * @deprecated no current use
	 * @param pressedBackgroungColor
	 */
	@Deprecated
	public void setPressedBackgroungColor(Color pressedBackgroungColor) {
		this.pressedBackgroungColor = pressedBackgroungColor;
	}

	/**
	 * return the hover Icon of the button
	 * @return Icon
	 */
	public Icon getHoverIcon() {
		return hoverIcon;
	}

	/**
	 * set icon used when the button is hover
	 * @param hoverIcon
	 */
	public void setHoverIcon(Icon hoverIcon) {
		this.hoverIcon = hoverIcon;
	}

	/**
	 * @deprecated no current use
	 * @return Icon
	 */
	@Deprecated
	public Icon getPressedIcon() {
		return pressedIcon;
	}

	/**
	 * @deprecated no current use
	 * @param pressedIcon
	 */
	@Deprecated
	public void setPressedIcon(Icon pressedIcon) {
		this.pressedIcon = pressedIcon;
	}

	/**
	 * set x the horizontal position
	 * @param x integer represent relative position to the component
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * set y the horizontal position
	 * @param y integer represent relative position to the component
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * set width of the component
	 * @param width of the component
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * set height of the component
	 * @param height of the component
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * set if the button is hover or not
	 * @param hover
	 */
	public void setHover(boolean hover) {
		this.hover = hover;
	}

}
