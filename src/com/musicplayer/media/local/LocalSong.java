package com.musicplayer.media.local;

import java.net.URI;
import java.net.URISyntaxException;

import com.musicplayer.media.Song;

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaParsedStatus;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class LocalSong implements Song{

	private final Object NULL_RETURN 	= null;
	private final String FAILED_FORMAT	= "Song at location %s couldn't be parsed (failed)";
	private final String TIMEOUT_FORMAT = "Song at location %s couldn't be parsed (timeout)";
	private final String NO_URI_FORMAT 	= "Song at location %s doesn't have a valid URI (exception thrown)";
	
	// misc. variables
	private boolean parsed;
	private AudioPlayerComponent parser;
	// track infos
	private String 	location;
	private String 	title;
	private String 	artist;
	private String	albumArtist;
	private String 	album;
	private String 	date;
	private URI 	coverUri;
	
	/**
	 * Build a song object from a song path.
	 * @param path : path to the music file
	 */
	public LocalSong(String path) {
		parsed = false;
		location = path;
		parser = new AudioPlayerComponent();
		// prepare the media and parse it
		parser.mediaPlayer().media().prepare(path);
		parser.mediaPlayer().media().parsing().parse();
		// parsing event handling
		parser.mediaPlayer().events().addMediaEventListener(new MediaEventAdapter() {
			public void mediaParsedChanged(Media media, MediaParsedStatus status) {
				if(status == MediaParsedStatus.FAILED) {
					// if parsing failed
					throw new IllegalArgumentException(String.format(FAILED_FORMAT, location));
				} else if(status == MediaParsedStatus.TIMEOUT) {
					// if parsing timed out
					throw new IllegalArgumentException(String.format(TIMEOUT_FORMAT, location));
				} else if(status == MediaParsedStatus.DONE) {
					title 		= media.meta().get(Meta.TITLE);
					artist 		= media.meta().get(Meta.ARTIST);
					albumArtist = media.meta().get(Meta.ALBUM_ARTIST);
					album 		= media.meta().get(Meta.ALBUM);
					date		= media.meta().get(Meta.DATE);
					// first, get the uri in a string
					String _uri = media.meta().get(Meta.ARTWORK_URL);
					// if the uri isn't null, parse it and set it to our variable
					try {
						coverUri = (_uri == null) ? null : new URI(_uri);
					} catch (URISyntaxException e) {
						// if it fails, inform the user
						// no need to throw an exception, as songs can have no embedded art
						System.out.println(String.format(NO_URI_FORMAT, location));
						// the cover uri is then null
						coverUri = null;
					}
					// release every VLCJ component, as they aren't needed anymore
					media.release();
					releasePlayer();
					// parsing done!
					parsed = true;
				}
			}
		});
	}
	
	/**
	 * get the title of the song
	 */
	@Override
	public String getTitle() {
		return (String)protect(title);
	}

	/**
	 * get the artist of the song
	 */
	@Override
	public String getArtist() {
		return (String)protect(artist);
	}

	/**
	 * get the location of the song
	 */
	@Override
	public String getLocation() {
		return location;
	}
	
	/**
	 * get the URI of the cover art, if it exists
	 */
	@Override
	public URI getCoverUri() {
		return (URI)protect(coverUri);
	}
	
	/**
	 * get the album artist
	 */
	@Override
	public String getAlbumArtist() {
		return (String)protect(albumArtist);
	}

	/**
	 * get the album name
	 */
	@Override
	public String getAlbum() {
		return (String)protect(album);
	}

	/**
	 * get the date
	 */
	@Override
	public String getDate() {
		return (String)protect(date);
	}
	
	public String toString() {
		return location;
	}
	
	/**
	 * Return null (can be set via class constant) if the song wasn't parsed yet or the object is null, returns the parsed object otherwise
	 * @param o : object to return (or not)
	 * @return o if song was parsed and the object isn't null, null otherwise
	 */
	private Object protect(Object o) {
		if(parsed || o == null)
			return NULL_RETURN;
		else
			return o;
	}
	
	/**
	 * Release the player and AudioPlayerComponent
	 */
	private void releasePlayer() {
		if(parser != null) {
			// first; release the media player
			// It is not allowed to call back into LibVLC from an event handling thread, so submit() is used
	        parser.mediaPlayer().submit(new Runnable() {
	            @Override
	            public void run() {
	                parser.mediaPlayer().release();
	            }
	        });
	        // release, the whole component then.
	        parser.release();
		}
	}
}
