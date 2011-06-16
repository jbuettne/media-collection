package com.mediacollector.collection.audio;

public interface TrackTbl {

	public static final String COL_TRACK_ID = "mbId";
	public static final String COL_TRACK_NAME = "name";
	public static final String COL_TRACK_ARTIST = "artist";
	public static final String COL_TRACK_CD = "cd";
	public static final String COL_TRACK_TRACKONCD = "trackOnCd";
	public static final String COL_TRACK_LENGTH = "length";

	static final String TABLE_NAME = "Track";

	static final String SQL_CREATE = "CREATE TABLE Track (				"
			+ "	mbId		VARCHAR(63)		PRIMARY KEY,				"
			+ "	name		VARCHAR(500)	NOT NULL,					"
			+ "	artist		INTEGER	 		REFERENCES Artist,			"
			+ "	cd			INTEGER	 		REFERENCES Album,			"
			+ "	trackOnCd	INTEGER(3),									"
			+ "	length		INTEGER(6)									"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO Track (		"
			+ "	mbId, name, artist, cd, trackOnCd, length) "
			+ " 	values (?,?,?,?,?,?)";
}