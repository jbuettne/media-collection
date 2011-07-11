package com.mediacollector.collection.audio;

public interface AlbumTbl {

	public static final String COL_ALBUM_ID = "id";
	public static final String COL_ALBUM_NAME = "name";
	public static final String COL_ALBUM_ARTIST = "artist";
	public static final String COL_ALBUM_YEAR = "year";
	public static final String COL_ALBUM_IMAGE = "imgPath";
	public static final String COL_ALBUM_IMAGE_HTTP = "imgPathHttp";

	static final String TABLE_NAME = "Album";

	static final String SQL_CREATE = "CREATE TABLE Album ( 					"
			+ "id		VARCHAR(63)		PRIMARY KEY, 						"
			+ "name	VARCHAR(500)	PRIMARY KEY NOT NULL, 					"
			+ "artist	VARCHAR(63), 										"
			+ "year	VARCHAR(4), 											"
			+ "imgPath	VARCHAR(500), 										"
			+ "imgPathHttp	VARCHAR(500), 									"
			+ "FOREIGN KEY(artist) REFERENCES Artist(name) ON DELETE CASCADE"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO Album ( 			"
			+ "id, name, artist, year, imgPath, imgPathHttp)				"
			+ " 	values (?,?,?,?,?,?)";

	static final String STMT_TRIGGER = "CREATE TRIGGER noAlbum 				" 
			+ "AFTER DELETE ON Album 										" 
			+ "BEGIN 														" 
			+ "		DELETE FROM Artist WHERE Artist.name NOT IN 			"
			+ "			(SELECT artist FROM Album); 						" 
			+ "END;";
}