package com.musicplayer.scanner;

import java.util.List;

import com.musicplayer.media.Album;
import com.musicplayer.media.Song;

public interface Scanner {
	List<Album> getAlbums();
	List<Song> getSongs();
}
