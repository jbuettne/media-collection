package com.mediacollector.collection.video;

/**
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public interface FilmTbl {

		public static final String COL_FILM_ID = "id";
		public static final String COL_FILM_NAME = "name";
		public static final String COL_FILM_YEAR = "year";
		public static final String COL_FILM_IMAGE = "imgPath";
		public static final String COL_FILM_IMAGE_HTTP = "imgPathHttp";

		static final String TABLE_NAME = "Film";

		static final String SQL_CREATE = "CREATE TABLE Film (				"
				+ "	id		VARCHAR(63)		PRIMARY KEY,					"
				+ "	name	VARCHAR(500)	NOT NULL,						"
				+ "	year	VARCHAR(4),										"
				+ "	imgPath	VARCHAR(500),									"
				+ "	imgPathHttp	VARCHAR(500)								"
				+ ");";

		static final String STMT_FULL_INSERT = "INSERT INTO Film (			"
				+ "	id, name, year, imgPath, imgPathHttp)					"
				+ " 	values (?,?,?,?,?)";
		
		static final String STMT_DELETE = "DELETE FROM Film WHERE ?";
	}