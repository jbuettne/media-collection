package com.mediacollector.collection.audio;

public interface ArtistTbl {

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