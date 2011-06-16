package com.mediacollector.collection.audio;

public class Album {

	public String mbId;
  
	public String name;

	public String artist;

	public long year;

	public String imgPath;
	
	public Album(String mbId, String name, String artist, long year, 
			String imgPath){
		this.mbId = mbId;
		this.name = name;
		this.artist = artist;
		this.year = year;
		this.imgPath = imgPath;
	}
	public Album(){
		
	}
}

