package com.mediacollector.collection.books;

public class Book {
		  
	public String id;
	
	public String name;

	public String author;

	public String year;

	public String imgPath;

	public String imgPathHttp;
	
	public Book(String id, String name, String author, String year, 
			String imgPath, String imgPathHttp){
		this.id = id;
		this.name = name;
		this.author = author;
		this.year = year;
		this.imgPath = imgPath;
		this.imgPathHttp = imgPathHttp;
	}
	public Book(){
		
	}		

}
