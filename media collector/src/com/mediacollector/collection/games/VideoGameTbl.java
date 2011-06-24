package com.mediacollector.collection.games;

public interface VideoGameTbl {

	public static final String COL_VIDEO_ID = "_id";
	public static final String COL_VIDEO_NAME = "name";
	public static final String COL_VIDEO_YEAR = "year";
	public static final String COL_VIDEO_IMGPATH = "imgPath";

	static final String TABLE_NAME = "VideoGame";

	static final String SQL_CREATE = "CREATE TABLE VideoGame (			"
			+ "	_id			INTEGER		PRIMARY KEY AUTO INCREMENT,		"
			+ "	name		VARCHAR(500)	NOT NULL,					"
			+ "	platform		VARCHAR(500)	NOT NULL,				"
			+ "	year		INTEGER(4),									"
			+ "	imgPath		VARCHAR(500)								"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO VideoGame (		"
			+ "	name, platform, year, imgPath) "
			+ " 	values (?,?,?,?)";
	
}
