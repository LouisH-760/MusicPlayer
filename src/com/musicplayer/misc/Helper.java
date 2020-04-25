package com.musicplayer.misc;

/**
 * Various shared functions
 * @author Louis Hermier
 *
 */
public class Helper {
	private static final String DEFAULT_STR = "-1";
	public static final String SEP = "/";
	
	/**
	 * Check if a condition is respected. If not, raise an exception
	 * @param condition > boolean, exceptions thrown if false
	 * @param message > String, passed to the exceptions thrown
	 */
	public static void check(boolean condition, String message) {
		if(!condition) throw new IllegalArgumentException(message);
	}
	
	/**
	 * Extract the file extension from a given path. Should work even if the path includes dots
	 * @param path
	 * @return string: extension without dot (eg "flac")
	 */
	public static String getExtension(String path) {
		String extension = DEFAULT_STR;
		int i = path.lastIndexOf('.');
		int p = Math.max(path.lastIndexOf(SEP), path.lastIndexOf('\\'));

		if (i > p) 
		    extension = path.substring(i+1);
		
		return (extension.equals(DEFAULT_STR)) ? null : extension;
	}
	
	/**
	 * Check if a path ends with a "/".
	 * if not, adds it
	 * @param path > path to check
	 * @return fixed path
	 */
	public static String trailingSep(String path) {
		return (path.substring(path.length() - SEP.length()).contentEquals(SEP)) ? path : path + SEP;
	}
}
