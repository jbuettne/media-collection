package com.mediacollector.collection.games;

/**
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public class BoardGame {
		  
	public String id;
	
	public String name;

	public String year;

	public String imgPath;

	public String imgPathHttp;
	
	
	public BoardGame(String id, String name, String year, String imgPath,
			String imgPathHttp){
		this.id = id;
		this.name = name;
		this.year = year;
		this.imgPath = imgPath;
		this.imgPathHttp = imgPathHttp;
	}
	public BoardGame(){
		
	}		

}
