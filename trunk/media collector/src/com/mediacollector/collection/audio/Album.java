package com.mediacollector.collection.audio;

public class Album {

	public String mbId;
  
	public String name;

	public String artist;

	public String year;

	public String imgPath;
	
	public String imgPathHttp;
	
	public Album(String mbId, String name, String artist, String year, 
			String imgPath, String imgPathHttp){
		this.mbId = mbId;
		this.name = name;
		this.artist = artist;
		this.year = year;
		this.imgPath = imgPath;
		this.imgPathHttp = imgPathHttp;
	}
	public Album(){
		
	}
}

