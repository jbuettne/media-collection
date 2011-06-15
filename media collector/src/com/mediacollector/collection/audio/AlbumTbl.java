package com.mediacollector.collection.audio;

public interface AlbumTbl {

	public static final String COL_ALBUM_ID = "_id";
	public static final String COL_ALBUM_NAME = "name";
	public static final String COL_ALBUM_ARTIST = "artist";
	public static final String COL_ALBUM_YEAR = "year";
	public static final String COL_ALBUM_IMAGE = "imgPath";
	public static final String COL_ALBUM_MBID = "mbId";

	static final String TABLE_NAME = "Album";

	static final String SQL_CREATE = "CREATE TABLE Album (				"
			+ "	_id		INTEGER			PRIMARY KEY AUTOINCREMENT,		"
			+ "	name	VARCHAR(500)	NOT NULL,						"
			+ "	artist	INTEGER 		REFERENCES Artist,				"
			+ "	year	INTEGER(4),										"
			+ "	imgPath	VARCHAR(500),									"
			+ "	mbId	VARCHAR(500)									"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO Album (			"
			+ "	name, artist, year, imgPath, mbId)  					"
			+ " 	values (?,?,?,?,?)";
}