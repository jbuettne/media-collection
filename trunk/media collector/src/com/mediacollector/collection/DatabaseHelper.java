package com.mediacollector.collection;

import com.mediacollector.collection.audio.AlbumTbl;
import com.mediacollector.collection.audio.ArtistTbl;
import com.mediacollector.collection.audio.TrackTbl;

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
		db.execSQL(TrackTbl.SQL_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ArtistTbl.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AlbumTbl.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TrackTbl.TABLE_NAME);
		onCreate(db);
	}
}
