package com.mediacollector.collection.video;

public class Film {

	public String id;
  
	public String name;

	public String year;

	public String imgPath;

	public String imgPathHttp;
	
	public Film(String id, String name, String year, String imgPath, 
			String imgPathHttp){
		this.id = id;
		this.name = name;
		this.year = year;
		this.imgPath = imgPath;
		this.imgPathHttp = imgPathHttp;
	}
	public Film(){
		
	}

}
