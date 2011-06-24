package com.mediacollector.collection.books;

public class Book {
		  
	public String name;

	public String author;

	public long year;

	public String imgPath;
	
	public Book(String name, String author, long year, String imgPath){
		this.name = name;
		this.author = author;
		this.year = year;
		this.imgPath = imgPath;
	}
	public Book(){
		
	}		

}
