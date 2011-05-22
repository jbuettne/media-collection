package com.mediacollector.collection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATENBANK_NAME = "mcData.db";
	private static final int DATENBANK_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATENBANK_NAME, null, DATENBANK_VERSION);
	}

	public interface T_Artist {

		public static final String COL_ARTIST_ID = "_id";
		public static final String COL_ARTIST_NAME = "name";
		public static final String COL_ARTIST_IMAGE = "imgPath";
		public static final String COL_ARTIST_MBID = "mbId";

		static final String TABLE_NAME = "Artist";

		static final String SQL_CREATE = "CREATE TABLE Artist (				"
				+ "	_id		INTEGER			PRIMARY KEY AUTOINCREMENT,		"
				+ "	name	VARCHAR(500)	NOT NULL,						"
				+ "	imgPath	VARCHAR(500),									"
				+ "	mbId	VARCHAR(500)									"
				+ ");";

		static final String STMT_FULL_INSERT = "INSERT INTO Artist (		"
				+ "	name, imgPath, mbId)  									"
				+ " 	values (?,?,?)";
		
		static final String STMT_DELETE = "DELETE FROM Artist WHERE ?";
	}

	public interface T_Cd {

		public static final String COL_CD_ID = "_id";
		public static final String COL_CD_NAME = "name";
		public static final String COL_CD_ARTIST = "artist";
		public static final String COL_CD_YEAR = "year";
		public static final String COL_ARTIST_IMAGE = "imgPath";
		public static final String COL_ARTIST_MBID = "mbId";

		static final String TABLE_NAME = "Cd";

		static final String SQL_CREATE = "CREATE TABLE Cd (					"
				+ "	_id		INTEGER			PRIMARY KEY AUTOINCREMENT,		"
				+ "	name	VARCHAR(500)	NOT NULL,						"
				+ "	artist	INTEGER 		REFERENCES Artist,				"
				+ "	year	INTEGER,										"
				+ "	imgPath	VARCHAR(500),									"
				+ "	mbId	VARCHAR(500)									"
				+ ");";

		static final String STMT_FULL_INSERT = "INSERT INTO Cd (			"
				+ "	name, artist, year, imgPath, mbId)  					"
				+ " 	values (?,?,?,?,?)";
	}

	public interface T_Track {

		public static final String COL_TRACK_ID = "_id";
		public static final String COL_TRACK_NAME = "name";
		public static final String COL_TRACK_ARTIST = "artist";
		public static final String COL_TRACK_CD = "cd";
		public static final String COL_TRACK_TRACKONCD = "trackOnCd";
		public static final String COL_TRACK_LENGTH = "length";
		public static final String COL_TRACK_IMAGE = "imgPath";
		public static final String COL_TRACK_MBID = "mbId";

		static final String TABLE_NAME = "Track";

		static final String SQL_CREATE = "CREATE TABLE Track (				"
				+ "	_id			INTEGER			PRIMARY KEY AUTOINCREMENT,	"
				+ "	name		VARCHAR(500)	NOT NULL,					"
				+ "	artist		INTEGER 		REFERENCES Artist,			"
				+ "	cd			INTEGER 		REFERENCES Cd,				"
				+ "	trackOnCd	INTEGER,									"
				+ "	length		INTEGER,									"
				+ "	imgPath		VARCHAR(500),								"
				+ "	mbId		VARCHAR(500)								"
				+ ");";

		static final String STMT_FULL_INSERT = "INSERT INTO Track (		"
				+ "	name, artist, cd, trackOnCd, length, imgPath, mbId) "
				+ " 	values (?,?,?,?,?,?,?)";
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(T_Artist.SQL_CREATE);
		db.execSQL(T_Cd.SQL_CREATE);
		db.execSQL(T_Track.SQL_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + T_Artist.TABLE_NAME);
	    db.execSQL("DROP TABLE IF EXISTS " + T_Cd.TABLE_NAME);
	    db.execSQL("DROP TABLE IF EXISTS " + T_Track.TABLE_NAME);
		onCreate(db);
	}
}
