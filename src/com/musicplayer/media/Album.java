package com.musicplayer.media;

import java.net.URI;

public interface Album {
	String getName();
	String getArtist();
	String getNumberOfSongs();
	Song[] getSongs();
	Song getSong(int index);
	String getDate();
	// return the formatted date for display
	URI getCoverArtUri();
}
