package com.mediacollector.collection.games;

public interface BoardGameTbl {

	public static final String COL_BOARD_ID = "_id";
	public static final String COL_BOARD_NAME = "name";
	public static final String COL_BOARD_YEAR = "year";
	public static final String COL_BOARD_IMGPATH = "imgPath";

	static final String TABLE_NAME = "BoardGame";

	static final String SQL_CREATE = "CREATE TABLE BoardGame (				"
			+ "	_id			INTEGER		PRIMARY KEY AUTO INCREMENT,		"
			+ "	name		VARCHAR(500)	NOT NULL,					"
			+ "	year		INTEGER(4),									"
			+ "	imgPath		VARCHAR(500)								"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO BoardGame (		"
			+ "	name, year, imgPath) "
			+ " 	values (?,?,?)";
	
}
