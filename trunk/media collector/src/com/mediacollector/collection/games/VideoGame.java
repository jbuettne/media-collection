package com.mediacollector.collection.games;

public class VideoGame {
		  
	public String name;
	
	public String id;

	public String year;

	public String imgPath;
	
	public VideoGame(String id, String name, String year, String imgPath){
		this.id = id;
		this.name = name;
		this.year = year;
		this.imgPath = imgPath;
	}
	public VideoGame(){
		
	}		

}
