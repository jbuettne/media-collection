package com.mediacollector.collection.games;

public interface VideoGameTbl {

	public static final String COL_VIDEO_ID = "id";
	public static final String COL_VIDEO_NAME = "name";
	public static final String COL_VIDEO_YEAR = "year";
	public static final String COL_VIDEO_IMAGE = "imgPath";

	static final String TABLE_NAME = "VideoGame";

	static final String SQL_CREATE = "CREATE TABLE VideoGame (			"
			+ "	id			VARCHAR(63)		NOT NULL ,					"
			+ "	name		VARCHAR(500)	NOT NULL ,					"
			+ "	year		VARCHAR(4),									"
			+ "	imgPath		VARCHAR(500),								"
			+ " PRIMARY KEY (id, name)									"		
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO VideoGame (		"
			+ "	id, name, year, imgPath) "
			+ " 	values (?,?,?,?)";
	
}
