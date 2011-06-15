package com.mediacollector.collection.audio;

public class Artist {
  
	public String name;

	public long id;

	public String imgPath;

	public String mbId;

	public boolean istNeu() {
		return id == 0;
	}
	
	public long getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	public String getImgPath() {
		return imgPath;
	}
	
	public String getMbId() {
		return mbId;
	}
	
	public Artist(String name, String imgPath, String mbId){
		this.name = name;
		this.imgPath = imgPath;
		this.mbId = mbId;
	}
	public Artist(){
		
	}
}

