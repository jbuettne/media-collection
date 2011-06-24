package com.mediacollector.collection;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.mediacollector.Start;
import com.mediacollector.collection.audio.Album;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.AlbumTbl;
import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.collection.audio.ArtistTbl;
import com.mediacollector.collection.audio.TrackData;
import com.mediacollector.collection.books.AuthorData;
import com.mediacollector.collection.books.AuthorTbl;
import com.mediacollector.collection.books.BookData;
import com.mediacollector.collection.books.BookTbl;
import com.mediacollector.collection.video.FilmData;
import com.mediacollector.collection.video.FilmTbl;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Die Datenbank-Klasse, die für die Erstellung, zur Verbindung zur Datenbank
 * und zum sauberen Ausführen von Anfragen zuständig ist. Die Klasse ist als
 * Singleton aufgebaut, es ist also nur eine Verbindung zur Datenbank möglich.
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public class Database{

	private static final String TAG = "Database";
	private DatabaseHelper dbHelper;
	
	private ArtistData artist;
	private AlbumData album;
	private TrackData track;
	private BookData book;
	private AuthorData author;
	private FilmData film;


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
	
	public FilmData getFilm() {
		return film;
	}

	public Database(Context context) {
		artist = new ArtistData(context);
		album = new AlbumData(context);
		film = new FilmData(context);
		dbHelper = new DatabaseHelper(context);
//		book = new BookData(context);
//		author = new AuthorData(context);
		
	}
	
	public ArrayList<Data> getSearchResult(String search) {
		ArrayList<Data> searchResult = new ArrayList<Data>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT a.* , ar.name "						+
					"FROM " + AlbumTbl.TABLE_NAME + " As a " 	+
					"JOIN " + ArtistTbl.TABLE_NAME + " As ar " 	+
					"ON (a.artist = ar.mbId) " 					+
					"WHERE a.name LIKE  '%" + search + "%' " 	+
					"OR ar.name LIKE  '%" + search + "%'", null);
			if (dbCursor.moveToFirst() == true) {
		    	searchResult.add(new Data(dbCursor.getString(0),
		    			dbCursor.getString(1), dbCursor.getLong(3),
		    			dbCursor.getString(4), AlbumTbl.TABLE_NAME, 
		    			dbCursor.getString(5)));
		    	//artists.add(dbCursor.getString(0));
			    while (dbCursor.moveToNext() == true) {
			    	searchResult.add(new Data(dbCursor.getString(0),
			    			dbCursor.getString(1), dbCursor.getLong(3),
			    			dbCursor.getString(4), AlbumTbl.TABLE_NAME, 
			    			dbCursor.getString(5)));
			    }
			}	
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * "									+
					"FROM " + FilmTbl.TABLE_NAME				+
					" WHERE name LIKE  '%" + search + "%' ", null);
			if (dbCursor.moveToFirst() == true) {
		    	searchResult.add(new Data(dbCursor.getString(0),
		    			dbCursor.getString(1), dbCursor.getLong(2),
		    			dbCursor.getString(3), FilmTbl.TABLE_NAME, 
		    			""));
		    	//artists.add(dbCursor.getString(0));
			    while (dbCursor.moveToNext() == true) {
			    	searchResult.add(new Data(dbCursor.getString(0),
			    			dbCursor.getString(1), dbCursor.getLong(2),
			    			dbCursor.getString(3), FilmTbl.TABLE_NAME, 
			    			""));
			    }
			}	
			if (searchResult.isEmpty()) {
				return new ArrayList<Data>();
			}
  
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return searchResult;
	}

	public final void writeCsv(String csvFile, String encoding) {
		final String[] dataTables = { ArtistTbl.TABLE_NAME,
				AlbumTbl.TABLE_NAME, BookTbl.TABLE_NAME, FilmTbl.TABLE_NAME };
		Cursor dbCursor = null;
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(csvFile), encoding));
			try {
				for (String table : dataTables) {
					dbCursor = dbHelper.getReadableDatabase().rawQuery(
							"SELECT * FROM " + table, null);
					while (dbCursor.moveToNext() == true) {
						out.write(table +";");
						for (int i = 0; i < dbCursor.getColumnCount(); i++) {
							try {
								out.write(dbCursor.getString(i) + ";");
							} catch (Throwable ex) {
								out.write(String.valueOf(dbCursor.getLong(i))
										+ ";");
							}
						}
						out.newLine();
					}
				}
				out.newLine();
			} finally {
				out.close();
				dbCursor.close();
			}
			Log.e(TAG, "\n" + csvFile + " erzeugt.");
		} catch (Exception ex) {
			Log.e(TAG, "\nFehler beim Erzeugen der CSV-Datei '"
					+ csvFile + "': " + ex);
		}
	}
	
	/**
	 * schlie�e die Datenbankverbindung
	 */
	public void closeConnection() {
		artist.close();
		album.close();
		track.close();
		film.close();
		dbHelper.close();
//		book.close();
//		author.close();
	}

}
