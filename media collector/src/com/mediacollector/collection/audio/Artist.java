package com.mediacollector.collection.audio;

public class Artist {

	public String mbId;
	
	public String name;

	public String imgPath;
	
	public Artist(String mbId, String name, String imgPath){
		this.name = name;
		this.imgPath = imgPath;
		this.mbId = mbId;
	}
	public Artist(){
		
	}
}

