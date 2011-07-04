package com.mediacollector.collection.games;

public class BoardGame {
		  
	public String id;
	
	public String name;

	public long year;

	public String imgPath;
	
	
	public BoardGame(String id, String name, long year, String imgPath){
		this.id = id;
		this.name = name;
		this.year = year;
		this.imgPath = imgPath;
	}
	public BoardGame(){
		
	}		

}
