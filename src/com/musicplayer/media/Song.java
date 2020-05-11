package com.musicplayer.media;

public interface Song {
	String getTitle();
	String getArtist(); 
	// a song can have a different artist than the album
	// see splits, compilations, ...
}
