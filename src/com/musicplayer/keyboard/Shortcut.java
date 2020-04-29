package com.musicplayer.keyboard;

import java.awt.event.KeyEvent;

/**
 * Shortcut definition with masks, key, key action type, and runnable 
 * @author Vincent Carpentier
 *
 */
public class Shortcut {
	
	private KeyActionType[] keyActionTypes;
	private int[] keyEventMasks;
	private int keyEventVK;
	private Runnable action;
	
	/**
	 * @param keyActionTypes List of events type when the action can be done
	 * @param keyEventVK Key of the shortcut
	 * @param action Run when KeyEvent is teasted and verified
	 */
	public Shortcut(KeyActionType[] keyActionTypes, int keyEventVK, Runnable action) {
		this(keyActionTypes, null, keyEventVK, action);
	}
	
	/**
	 * @param keyActionTypes List of events type when the action can be done
	 * @param keyEventMask Keys masks that are necessary to do the shortcut
	 * @param keyEventVK Key of the shortcut
	 * @param action Run when KeyEvent is teasted and verified
	 */
	public Shortcut(KeyActionType[] keyActionTypes, int[] keyEventMask, int keyEventVK, Runnable action) {
		super();
		this.keyActionTypes = keyActionTypes;
		this.keyEventMasks = keyEventMask;
		this.keyEventVK = keyEventVK;
		this.action = action;
	}
	
	/**
	 * @param event
	 * Check masks and key and run action if all keys were verified
	 * This method DO NOT CHECK if it is right KeyActionType
	 */
	public void check (KeyEvent event) {
		int modif = event.getModifiers();
		for (int keyEventMask : keyEventMasks)
			modif ^= keyEventMask;
		if (modif == 0 && keyEventVK == event.getKeyCode())
			action.run();
	}
	
	/**
	 * @param keyActionType 
	 * @return true if has keyActionType, else false
	 */
	public boolean hasKeyActionType (KeyActionType keyActionType) {
		for (KeyActionType kat : keyActionTypes)
			if (kat == keyActionType)
				return true;
		return false;
	}
}
