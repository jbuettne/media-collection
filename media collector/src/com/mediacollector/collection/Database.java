package com.mediacollector.collection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.AlbumTbl;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.collection.audio.ArtistTbl;
import com.mediacollector.collection.books.BookData;
import com.mediacollector.collection.books.BookTbl;
import com.mediacollector.collection.games.BoardGameData;
import com.mediacollector.collection.games.BoardGameTbl;
import com.mediacollector.collection.games.VideoGameData;
import com.mediacollector.collection.games.VideoGameTbl;
import com.mediacollector.collection.video.FilmData;
import com.mediacollector.collection.video.FilmTbl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
	private BookData book;
	private FilmData film;
	private BoardGameData board;
	private VideoGameData video;


	public ArtistData getArtist() {
		return artist;
	}

	public AlbumData getAlbum() {
		return album;
	}

	public BookData getBook() {
		return book;
	}
	
	public FilmData getFilm() {
		return film;
	}
	
	public BoardGameData getBoardGame() {
		return board;
	}
	
	public VideoGameData getVideoGame() {
		return video;
	}

	public Database(Context context) {
		dbHelper = new DatabaseHelper(context);
		artist = new ArtistData(context);
		album = new AlbumData(context);
		film = new FilmData(context);
		book = new BookData(context);
		board = new BoardGameData(context);
		video = new VideoGameData(context);
		
	}
	
    final String[] tables = {
    		FilmTbl.TABLE_NAME, BoardGameTbl.TABLE_NAME, VideoGameTbl.TABLE_NAME
    };
    
	public ArrayList<Data> getSearchResult(String search) {
		ArrayList<Data> searchResult = new ArrayList<Data>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT a.* , ar.name "						+
					"FROM " + AlbumTbl.TABLE_NAME + " As a " 	+
					"JOIN " + ArtistTbl.TABLE_NAME + " As ar " 	+
					"ON (a.artist = ar.id) " 					+
					"WHERE a.name LIKE  '%" + search + "%' " 	+
					"OR ar.name LIKE  '%" + search + "%'", null);
			if (dbCursor.moveToFirst() == true) {
				searchResult.add(new Data(dbCursor.getString(0), dbCursor
						.getString(1), dbCursor.getString(3), dbCursor
						.getString(4), AlbumTbl.TABLE_NAME, dbCursor
						.getString(5)));
			}
			while (dbCursor.moveToNext() == true) {
				searchResult.add(new Data(dbCursor.getString(0), dbCursor
						.getString(1), dbCursor.getString(3), dbCursor
						.getString(4), AlbumTbl.TABLE_NAME, dbCursor
						.getString(5)));
			}
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * " + "FROM " + BookTbl.TABLE_NAME
							+ " WHERE name LIKE  '%" + search
							+ "%' OR author LIKE '%" + search + "%'", null);
			if (dbCursor.moveToFirst() == true) {
				searchResult.add(new Data(dbCursor.getString(0), dbCursor
						.getString(1), dbCursor.getString(2), dbCursor
						.getString(3), BookTbl.TABLE_NAME, ""));
			}
			while (dbCursor.moveToNext() == true) {
				searchResult.add(new Data(dbCursor.getString(0), dbCursor
						.getString(1), dbCursor.getString(2), dbCursor
						.getString(3), BookTbl.TABLE_NAME, ""));
			}
			for (String table : tables) {
				dbCursor = dbHelper.getReadableDatabase().rawQuery(
						"SELECT * " + "FROM " + table + " WHERE name LIKE  '%"
								+ search + "%' ", null);
				if (dbCursor.moveToFirst() == true) {
					searchResult.add(new Data(dbCursor.getString(0), dbCursor
							.getString(1), dbCursor.getString(2), dbCursor
							.getString(3), table, ""));
				}
				while (dbCursor.moveToNext() == true) {
					searchResult.add(new Data(dbCursor.getString(0), dbCursor
							.getString(1), dbCursor.getString(2), dbCursor
							.getString(3), table, ""));
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

	public final void writeToCsv(String csvFile, String encoding) {
		final String[] dataTables = { ArtistTbl.TABLE_NAME, FilmTbl.TABLE_NAME,
				AlbumTbl.TABLE_NAME, BookTbl.TABLE_NAME,
				BoardGameTbl.TABLE_NAME, VideoGameTbl.TABLE_NAME};
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
	
	
	public final void readFromCsv(String filePath) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath)));
			String readString;
			while ((readString = in.readLine()) != null) {
				String[] tmpArray = readString.split(";");
				if (tmpArray[0].equals("Artist")) {
					artist.insertArtist(tmpArray[1], tmpArray[2]);
				} else if(tmpArray[0].equals("Album")) {
					album.insertAlbum(tmpArray[1], tmpArray[2], tmpArray[3],
							tmpArray[4], tmpArray[5]);
				} else if(tmpArray[0] == "Book") {
					book.insertBook(tmpArray[1], tmpArray[2], tmpArray[3],
							tmpArray[4], tmpArray[5]);
				} else if(tmpArray[0] == "Film") {
					film.insertFilm(tmpArray[1], tmpArray[2],
							tmpArray[3], tmpArray[4]);
				} else if(tmpArray[0] == "BoardGame") {
					board.insertBoardGame(tmpArray[1], tmpArray[2],
							tmpArray[3], tmpArray[4]);
				} else if(tmpArray[0] == "VideoGame") {
					video.insertVideoGame(tmpArray[1], tmpArray[2],
							tmpArray[3], tmpArray[4]);
				}
			}
			in.close();
		} catch (Exception ex) {
			Log.e(TAG, "\nFehler beim Lesen der CSV-Datei '" + filePath + "': "
					+ ex);
		}

	}
	
	
	
	/**
	 * schlie�e die Datenbankverbindung
	 */
	public void closeConnection() {
		artist.close();
		album.close();
		film.close();
		book.close();
		board.close();
		video.close();
		dbHelper.close();
	}

}
