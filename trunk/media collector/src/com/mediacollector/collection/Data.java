package com.mediacollector.collection;

/**
 * 
 * @author Jens Buettner
 */
public class Data {

	public String id;
	public String name;
	public long year;
	public String imgPath;
	public String table;
	public String extra;
	
	public Data(String id, String name, long year, String imgPath,
			String table, String extra){
		this.id = id;
		this.name = name;
		this.year = year;
		this.imgPath = imgPath;
		this.table = table;
		this.extra = extra;
	}
}