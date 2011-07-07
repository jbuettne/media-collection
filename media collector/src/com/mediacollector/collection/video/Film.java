package com.mediacollector.collection.video;

public class Film {

	public String id;
  
	public String name;

	public String year;

	public String imgPath;
	
	public Film(String id, String name, String year, 
			String imgPath){
		this.id = id;
		this.name = name;
		this.year = year;
		this.imgPath = imgPath;
	}
	public Film(){
		
	}

}
