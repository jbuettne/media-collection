package com.mediacollector.collection.audio;

public interface AlbumTbl {

	public static final String COL_ALBUM_ID = "id";
	public static final String COL_ALBUM_NAME = "name";
	public static final String COL_ALBUM_ARTIST = "artist";
	public static final String COL_ALBUM_YEAR = "year";
	public static final String COL_ALBUM_IMAGE = "imgPath";

	static final String TABLE_NAME = "Album";

	static final String SQL_CREATE = "CREATE TABLE Album (				"
			+ "	id		VARCHAR(63)		PRIMARY KEY ,					"
			+ "	name	VARCHAR(500)	NOT NULL,						"
			+ "	artist	VARCHAR(63) 	REFERENCES Artist(mbId)         " 
			+ " 	ON DELETE CASCADE ON UPDATE CASCADE,				"
			+ "	year	INTEGER(4),										"
			+ "	imgPath	VARCHAR(500)									"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO Album (			"
			+ "	id, name, artist, year, imgPath)  					"
			+ " 	values (?,?,?,?,?)";
}