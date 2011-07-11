package com.mediacollector.collection.audio;

/**
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public interface ArtistTbl {

		public static final String COL_ARTIST_ID = "id";
		public static final String COL_ARTIST_NAME = "name";

		static final String TABLE_NAME = "Artist";

		static final String SQL_CREATE = "CREATE TABLE Artist (				"
				+ "id	VARCHAR(63)		, 									"
				+ "name	VARCHAR(500)	NOT NULL PRIMARY KEY				"
				+ ");";

		static final String STMT_FULL_INSERT = "INSERT INTO Artist (id, name) "
				+ " 	values (?,?)";
		
		static final String STMT_DELETE = "DELETE FROM Artist WHERE ?";
		
	}