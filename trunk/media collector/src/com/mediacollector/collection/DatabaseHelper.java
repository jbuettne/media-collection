package com.mediacollector.collection;

import com.mediacollector.collection.audio.AlbumTbl;
import com.mediacollector.collection.audio.ArtistTbl;
import com.mediacollector.collection.audio.TrackTbl;
import com.mediacollector.collection.books.BookTbl;
import com.mediacollector.collection.games.BoardGameTbl;
import com.mediacollector.collection.games.VideoGameTbl;
import com.mediacollector.collection.video.FilmTbl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "DatabaseHelper";
	private static final String DATENBANK_NAME = "mcData.db";
	private static final int DATENBANK_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATENBANK_NAME, null, DATENBANK_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ArtistTbl.SQL_CREATE);
		db.execSQL(AlbumTbl.SQL_CREATE);
		db.execSQL(BookTbl.SQL_CREATE);
		db.execSQL(FilmTbl.SQL_CREATE);
		db.execSQL(BoardGameTbl.SQL_CREATE);
		db.execSQL(VideoGameTbl.SQL_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ArtistTbl.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AlbumTbl.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + BookTbl.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + FilmTbl.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + BoardGameTbl.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + VideoGameTbl.TABLE_NAME);
		onCreate(db);
	}
}
