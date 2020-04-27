package com.musicplayer.keyboard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Key binding to use shortcuts
 * @author Vincent Carpentier
 *
 */
public class ShortcutBinding {
	
	private InputMap inputMap;
	private ActionMap actionMap;
	private int id;
	
	/**
	 * Get jframe content pane inputMap (the JComponent.WHEN_IN_FOCUSED_WINDOW) and actionMap
	 * @param jframe
	 */
	public ShortcutBinding(JFrame jframe) {
		this((JPanel)jframe.getContentPane());
	}
	
	/**
	 * Get jpanel content pane inputMap (the JComponent.WHEN_IN_FOCUSED_WINDOW) and actionMap
	 * @param jpanel
	 */
	public ShortcutBinding(JPanel jpanel) {
		this(jpanel, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	/**
	 * Get jframe content pane inputMap (the inputMapType) and actionMap
	 * @param jframe
	 * @param inputMapType
	 */
	public ShortcutBinding(JFrame jframe, int inputMapType) {
		this((JPanel)jframe.getContentPane(), inputMapType);
	}
	
	/**
	 * Get jpanel content pane inputMap (the inputMapType) and actionMap
	 * @param jpanel
	 * @param inputMapType
	 */
	public ShortcutBinding(JPanel jpanel, int inputMapType) {
		inputMap = jpanel.getInputMap(inputMapType);
		actionMap = jpanel.getActionMap();
	}
	
	
	/**
	 * Add key and modifier to inputMap and calls r in the actionMap
	 * Do not overwrite existant action with id preexistant
	 * @param keyCode usage example : KeyEvent.VK_SPACE
	 * @param modifiers usage example : KeyEvent.CTRL_MASK | KeyEvent.ALT_MASK
	 * @param r
	 */
	public void addBind(int keyCode, int modifiers, Runnable r){
		while(isIn(actionMap.keys(), id)) id++;
		addEvent(id, keyCode, modifiers);
		addAction(id, r);
		id++;
	}
	
	/**
	 * Check if id is in Object[] os
	 * @param os
	 * @param id
	 * @return
	 */
	private boolean isIn (Object[] os, int id) {
		if(os != null) {
			for (Object o : os)
				if (o instanceof Integer)
					if (((Integer)o).intValue() == id)
						return true;
		}
		return false;
	}
	
	/**
	 * Key and modifier to call acitonMap with the id
	 * @param id reference to the action in action map
	 * @param keyCode usage example : KeyEvent.VK_SPACE
	 * @param modifiers usage example : KeyEvent.CTRL_MASK | KeyEvent.ALT_MASK
	 */
	public void addEvent(int id, int keyCode, int modifiers){
		inputMap.put(KeyStroke.getKeyStroke(keyCode, modifiers), id);
	}
	
	/**
	 * Used to put action into actionMap
	 * @param id reference of the action
	 * @param r action
	 */
	public void addAction(int id, Runnable r){
		actionMap.put(id, new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				r.run();
			}
		});
	}
}
