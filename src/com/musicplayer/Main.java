package com.musicplayer;

import com.musicplayer.logic.Logic;
import com.musicplayer.logic.SingleAlbumLogic;

/**
 * Main method: create an instance of the wanted logic and start it
 * 
 * @author Louis Hermier
 *
 */
public class Main {
	private static Logic logic;
	
	public static void main(String[] args) throws InterruptedException {
		try {
			logic = new SingleAlbumLogic(args);
			logic.start();
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}
}
