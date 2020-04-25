package com.musicplayer.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.musicplayer.misc.Helper;

/**
 * Get stuff to play by scanning a folder
 * @author louis Hermier
 *
 */
public class FolderScanner implements Scanner{
	private static final String DIR_MSG = "%s isn't a valid directory.";
	private static final String EXIST_MSG = "%s doesn't exist.";
	
	private static final String COVER_REGEX = "(\\/.*\\/)*([Cc](over|OVER))([^\\/]*)";
	
	private static final String[] MUSIC_FORMATS = {"flac", "opus", "mp3", "wav", "ogg"}; // accepted music format
	// if using the VLCJ player, there are probably more.
	// As far as actually encountering them goes...
	// Your library should only be Flac / Opus and mp3 for the stuff you couldn't find lossless
	private static final String[] IMAGE_FORMATS = {"png", "jpg", "jpeg"}; // same here
	
	private String path;
	private String[] scan;
	
	/**
	 * Create the folder scanner, and run the preliminary scan on the folder
	 * @param path : path to an existing directory
	 */
	public FolderScanner(String path) {
		File file = new File(path);
		Helper.check(file.exists(), String.format(EXIST_MSG, path));
		Helper.check(file.isDirectory(), String.format(DIR_MSG, path));
		scan = file.list();
		Arrays.parallelSort(scan);
		this.path = Helper.trailingSep(path);
	}
	
	/**
	 * get all the _files_ in a directory
	 * @return arraylist of the files
	 */
	public ArrayList<String> getAllFiles() {
		ArrayList<String> out = new ArrayList<String>();
		for(String filename : scan) {
			File f = new File(path + filename);
			if(!(f.isDirectory())) {
				out.add(path + filename);
			}
		}
		return out;
	}
	
	/**
	 * get all the _subdirectories_ of the directory
	 * @return arraylist of the directories
	 */
	public ArrayList<String> getAllDirectories() {
		ArrayList<String> out = new ArrayList<String>();
		for(String filename : scan) {
			File f = new File(path + filename);
			if(f.isDirectory()) {
				out.add(path + filename);
			}
		}
		return out;
	}
	
	/**
	 * get all the _files_ matching a certain extension
	 * @param extensions extensions without the . eg "flac"
	 * @return arraylist of all the matching files
	 */
	public ArrayList<String> getAllFormatFiles(String[] extensions) {
		ArrayList<String> out = new ArrayList<String>();
		for(String file : getAllFiles()) {
			String fileExt = Helper.getExtension(file).toLowerCase();
			for(String ext : extensions) {
				if(ext.equals(fileExt)) {
					out.add(file);
				}
			}
		}
		return out;
	}
	
	/**
	 * uses a predefined format array to get all music files in the scan
	 */
	public ArrayList<String> getAllMusicFiles() {
		return getAllFormatFiles(MUSIC_FORMATS);
	}
	
	/**
	 * uses a predefined format to get all image files in the scan
	 */
	public ArrayList<String> getAllImageFiles() {
		return getAllFormatFiles(IMAGE_FORMATS);
	}
	
	/**
	 * uses a regex to get all files that should be cover art in the scan
	 */
	public ArrayList<String> getCoverArt() {
		ArrayList<String> images = getAllImageFiles();
		ArrayList<String> out = new ArrayList<String>();
		for(String image : images) {
			if(image.matches(COVER_REGEX)) {
				out.add(image);
			}
		}
		return out;
	}
	
	/**
	 * return the last folder name (without /) of the path to use as album name. 
	 * If you organize your library with picard, it should be an exact match except for any replaced characters
	 */
	public String getAlbumName() {
		String truncPath = path.substring(0, path.length() - 1);
		return truncPath.substring(truncPath.lastIndexOf("/") + 1, truncPath.length());
	}
	
	/**
	 * get the path that was passed to the ctor, debugging method
	 * @return path
	 */
	public String getPath() { return path; }
	/**
	 * return the results of the scan, debugging method
	 * @return scan
	 */
	public String[] getScan() { return scan; }
}
