package com.mediacollector.collection;

import java.io.IOException;

import com.mediacollector.Start;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.collection.audio.TrackData;
import com.mediacollector.collection.books.AuthorData;
import com.mediacollector.collection.books.BookData;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Die Datenbank-Klasse, die für die Erstellung, zur Verbindung zur Datenbank
 * und zum sauberen Ausführen von Anfragen zuständig ist. Die Klasse ist als
 * Singleton aufgebaut, es ist also nur eine Verbindung zur Datenbank möglich.
 * 
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Database{

	private ArtistData artist;
	private AlbumData album;
	private TrackData track;
	private BookData book;
	private AuthorData author;


	public ArtistData getArtist() {
		return artist;
	}

	public AlbumData getAlbum() {
		return album;
	}

	public TrackData getTrack() {
		return track;
	}

	public BookData getBook() {
		return book;
	}

	public AuthorData getAuthor() {
		return author;
	}

	public Database(Context context) {
		artist = new ArtistData(context);
		album = new AlbumData(context);
		track = new TrackData(context);
//		book = new BookData(context);
//		author = new AuthorData(context);
		
	}
	/**
	 * schlie�e die Datenbankverbindung
	 */
	public void closeConnection() {
		artist.close();
		album.close();
		track.close();
//		book.close();
//		author.close();
	}

}
