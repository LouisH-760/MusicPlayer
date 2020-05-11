package com.musicplayer.media;

import java.net.URI;
import java.util.List;

public interface Album {
	String getName();
	void setName(String name);
	String getArtist();
	void setArtist(String artist);
	int getNumberOfSongs();
	List<Song> getSongs();
	void addSong(Song song);
	void remSong(int index);
	Song getSong(int index);
	String getDate();
	void setDate(String date);
	// return the formatted date for display
	URI getCoverArtUri();
	void setCoverArtUri(URI coverUri);
}
