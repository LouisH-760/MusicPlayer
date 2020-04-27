package com.musicplayer.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


/**
 * AWT ShortcutListener, to check a list of shortcut
 * @author Vincent Carpentier
 *
 */
public class ShortcutListener implements KeyListener {
	
	private ArrayList<Shortcut> shortcuts = new ArrayList<>();

	@Override
	public void keyTyped(KeyEvent event) {
		checkShortcuts(event, KeyActionType.TYPED);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		checkShortcuts(event, KeyActionType.PRESSED);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		checkShortcuts(event, KeyActionType.RELEASED);
	}
	
	/**
	 * @param event
	 * @param keyActionType witch keyActionType need to be verified to check the shortcut
	 */
	private void checkShortcuts(KeyEvent event, KeyActionType keyActionType) {
		for (Shortcut shortcut : shortcuts)
			if (shortcut.hasKeyActionType(keyActionType))
				shortcut.check(event);
	}

	
	/**
	 * @param shortcut object to add to the shortcut list
	 * @return this object to do more operation on it
	 */
	public ShortcutListener addShortcut (Shortcut shortcut) {
		shortcuts.add(shortcut);
		return this;
	}
	
	/**
	 * Clear shortcut list
	 * @return this object to do more operation on it
	 */
	public ShortcutListener clearShortcut () {
		shortcuts.clear();
		return this;
	}

}
