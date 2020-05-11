package com.musicplayer.media;

import java.net.URI;
import java.util.List;

public interface Album {
	String getName();
	String getArtist();
	String getNumberOfSongs();
	List<Song> getSongs();
	Song getSong(int index);
	String getDate();
	// return the formatted date for display
	URI getCoverArtUri();
}
