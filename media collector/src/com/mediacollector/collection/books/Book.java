package com.mediacollector.collection.books;

public class Book {
		  
	public String id;
	
	public String name;

	public String author;

	public long year;

	public String imgPath;
	
	public Book(String id, String name, String author, long year, 
			String imgPath){
		this.id = id;
		this.name = name;
		this.author = author;
		this.year = year;
		this.imgPath = imgPath;
	}
	public Book(){
		
	}		

}
