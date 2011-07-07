package com.mediacollector.collection.audio;

public interface ArtistTbl {

		public static final String COL_ARTIST_ID = "mbId";
		public static final String COL_ARTIST_NAME = "name";
		public static final String COL_ARTIST_IMAGE = "imgPath";

		static final String TABLE_NAME = "Artist";

		static final String SQL_CREATE = "CREATE TABLE Artist (				"
				+ "	mbId	VARCHAR(63)		PRIMARY KEY ,					"
				+ "	name	VARCHAR(500)	NOT NULL						"
				+ ");";

		static final String STMT_FULL_INSERT = "INSERT INTO Artist (		"
				+ "	mbId, name)  									"
				+ " 	values (?,?)";
		
		static final String STMT_DELETE = "DELETE FROM Artist WHERE ?";
	}