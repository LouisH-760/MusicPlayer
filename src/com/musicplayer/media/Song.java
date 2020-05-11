package com.musicplayer.media;

import java.net.URI;

public interface Song {
	String getTitle();
	String getArtist(); 
	// a song can have a different artist than the album
	// see splits, compilations, ...
	String getLocation();
	// might need to be changed if stuff is to be done in a networked manner
	URI getCoverUri();
}
