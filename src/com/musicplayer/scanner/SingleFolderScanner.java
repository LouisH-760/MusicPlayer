package com.musicplayer.scanner;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.musicplayer.media.Album;
import com.musicplayer.media.Song;
import com.musicplayer.media.local.LocalAlbum;
import com.musicplayer.media.local.LocalSong;
import com.musicplayer.misc.Helper;

public class SingleFolderScanner implements Scanner {

	private static final String DEFAULT_IMAGE = "default.png";
	
	private static final String DIR_MSG = "%s isn't a valid directory.";
	private static final String EXIST_MSG = "%s doesn't exist.";
	
	private static final String COVER_REGEX = "(\\/.*\\/)*([Cc](over|OVER))([^\\/]*)";
	
	private static final String[] MUSIC_FORMATS = { "flac", "opus", "mp3", "wav", "ogg" }; // accepted music format
	// if using the VLCJ player, there are probably more.
	// As far as actually encountering them goes...
	// Your library should only be Flac / Opus and mp3 for the stuff you couldn't
	// find lossless
	private static final String[] IMAGE_FORMATS = { "png", "jpg", "jpeg" }; // same here
	
	private String path;
	private String[] scan;
	
	private Album album;
	private List<Song> songs;
	private List<Album> albums;
	private Song _song; // used as temporary song variable
	
	public SingleFolderScanner(String path) throws URISyntaxException {
		albums = new ArrayList<Album>();
		File file = new File(path);
		Helper.check(file.exists(), String.format(EXIST_MSG, path));
		Helper.check(file.isDirectory(), String.format(DIR_MSG, path));
		scan = file.list();
		Arrays.parallelSort(scan);
		this.path = Helper.trailingSep(path);
		parse();
	}
	
	@Override
	public List<Album> getAlbums() {
		return albums;
	}

	@Override
	public List<Song> getSongs() {
		return songs;
	}
	
	private void parse() throws URISyntaxException {
		// create LocalSong objects for each song
		for(String songPath : getAllMusicFiles()) {
			_song = new LocalSong(songPath);
			songs.add(_song);
		}
		// if there is one song or more and the first song isn't null, 
		// pull the metadata from it to add it to the album
		if(songs.size() > 0 && songs.get(0) != null) {
			_song = songs.get(0);
			album = new LocalAlbum(_song.getAlbum());
			album.setArtist(_song.getAlbumArtist());
			album.setDate(_song.getDate());
			// setting the album cover art:
			// - if there is embedded cover art in the song, use that
			// - else, if there is a cover in the folder, use that
			// - else, if there are any images in the folder, use that
			// - else, use the default picture
			if(_song.getCoverUri() != null) {
				album.setCoverArtUri(_song.getCoverUri());
			} else if(getCoverArt().size() > 0) {
				album.setCoverArtUri(new URI(getCoverArt().get(0)));
			} else if(getAllImageFiles().size() > 0) {
				album.setCoverArtUri(new URI(getAllImageFiles().get(0)));
			} else {
				album.setCoverArtUri(new URI(DEFAULT_IMAGE));
			}
		}
		albums.add(album);
	}
	
	
	/**
	 * get all the _files_ matching a certain extension
	 * 
	 * @param extensions
	 *            extensions without the . eg "flac"
	 * @return arraylist of all the matching files
	 */
	private ArrayList<String> getAllFormatFiles(String[] extensions) {
		ArrayList<String> out = new ArrayList<String>();
		for (String file : getAllFiles()) {
			String fileExt = Helper.getExtension(file).toLowerCase();
			for (String ext : extensions)
				if (ext.equals(fileExt)) {
					out.add(file);
					break; // A file has only one extension
				}
		}
		return out;
	}

	/**
	 * uses a predefined format array to get all music files in the scan
	 */
	private ArrayList<String> getAllMusicFiles() {
		return getAllFormatFiles(MUSIC_FORMATS);
	}

	/**
	 * uses a predefined format to get all image files in the scan
	 */
	private ArrayList<String> getAllImageFiles() {
		return getAllFormatFiles(IMAGE_FORMATS);
	}

	/**
	 * uses a regex to get all files that should be cover art in the scan
	 */
	private ArrayList<String> getCoverArt() {
		ArrayList<String> images = getAllImageFiles();
		ArrayList<String> out = new ArrayList<String>();
		for (String image : images)
			if (image.matches(COVER_REGEX))
				out.add(image);
		return out;
	}
	
	private ArrayList<String> getAllFiles() {
		ArrayList<String> out = new ArrayList<String>();
		for (String filename : scan)
			if (new File(path + filename).isFile())
				out.add(path + filename);
		return out;
	}

}
