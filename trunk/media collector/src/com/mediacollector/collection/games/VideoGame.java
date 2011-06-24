package com.mediacollector.collection.games;

public class VideoGame {
		  
	public String name;
	
	public String platform;

	public long year;

	public String imgPath;
	
	public VideoGame(String name, String platform, long year, String imgPath){
		this.name = name;
		this.platform = platform;
		this.year = year;
		this.imgPath = imgPath;
	}
	public VideoGame(){
		
	}		

}
