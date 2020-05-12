package com.musicplayer.media.local;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.musicplayer.media.Album;
import com.musicplayer.media.Song;
import com.musicplayer.misc.Helper;

public class LocalAlbum implements Album{

	private final String HIGH_FORMAT 	= "index must be < %d";
	private final String LOW_MSG		= "Index must be >= 0";
	
	private String name;
	private String artist;
	private String date;
	private URI coverUri;
	private String coverPath;
	private List<Song> songs;
	
	public LocalAlbum() {
		songs = new ArrayList<Song>();
	}
	
	public LocalAlbum(String name) {
		this();
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public int getNumberOfSongs() {
		return songs.size();
	}

	@Override
	public List<Song> getSongs() {
		return songs;
	}

	@Override
	public Song getSong(int index) {
		checkIndexRange(index);
		return songs.get(index);
	}

	@Override
	public String getDate() {
		return date;
	}

	@Override
	public URI getCoverArtUri() {
		return coverUri;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public void addSong(Song song) {
		songs.add(song);
	}

	@Override
	public void remSong(int index) {
		checkIndexRange(index);
		songs.remove(index);
	}

	@Override
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public void setCoverArtUri(URI coverUri) {
		this.coverUri = coverUri;
	}
	
	private void checkIndexRange(int index) {
		Helper.check(index >= 0, LOW_MSG);
		Helper.check(index < getNumberOfSongs(), String.format(HIGH_FORMAT, getNumberOfSongs()));
	}

	@Override
	public String getCoverArtPath() {
		return coverPath;
	}

	@Override
	public void setCoverArtPath(String path) {
		this.coverPath = path;
	}

}
