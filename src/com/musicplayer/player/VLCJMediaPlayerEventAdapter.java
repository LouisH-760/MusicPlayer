package com.musicplayer.player;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

/**
 * @author Vincent Carpentier
 *
 */
public class VLCJMediaPlayerEventAdapter extends MediaPlayerEventAdapter {

	private Runnable finishedAction;
	private Runnable positionUpdatedAction;

	@Override
	public void finished(MediaPlayer mediaPlayer) {
		new Thread(finishedAction).start();
	}

	@Override
	public void error(MediaPlayer mediaPlayer) {
		new Thread(finishedAction).start();
	}

	@Override
	public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
		if (positionUpdatedAction != null)
			new Thread(positionUpdatedAction).start();
	}

	public Runnable getFinishedAction() {
		return finishedAction;
	}

	public void setFinishedAction(Runnable finishedAction) {
		this.finishedAction = finishedAction;
	}

	public Runnable getPositionUpdatedAction() {
		return positionUpdatedAction;
	}

	public void setPositionUpdatedAction(Runnable positionUpdatedAction) {
		this.positionUpdatedAction = positionUpdatedAction;
	}

}
