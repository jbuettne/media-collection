package com.mediacollector.collection.audio;

public class Artist {
  
	public String name;

	public String imgPath;

	public String mbId;
		
	public String getName(){
		return name;
	}
	public String getImgPath() {
		return imgPath;
	}
	
	public String getMbId() {
		return mbId;
	}
	
	public Artist(String mbId, String name, String imgPath){
		this.name = name;
		this.imgPath = imgPath;
		this.mbId = mbId;
	}
	public Artist(){
		
	}
}

