package com.mediacollector.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mediacollector.fetching.Fetching;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.RegisteredActivity;
import com.mediacollector.tools.Exceptions.MCFetchingException;

public class FetchingTest extends RegisteredActivity implements Observer {
	
	private Resources resources;
	
	private String[] files = {"barcodes_audio","barcodes_books",
			"barcodes_games","barcodes_video","isbns_books"};
	
	private static final Pattern PATTERN_CODE = 
		Pattern.compile("([0-9]{7,14})[^ ]+");
	
	ArrayList<Fetching> fetcher = new ArrayList<Fetching>();
	
	private Fetching fetching;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Toast.makeText(this, "Test: wird gestartet", 	
        		Toast.LENGTH_LONG).show();

        /**for( String fileName : files ) {
	        try {
	        	fetching(fileName);
			} catch (MCFetchingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
        
        try {
			fetch("barcodes_books");
		} catch (MCFetchingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Toast.makeText(this, "Test: Fertig ", 
        		Toast.LENGTH_LONG).show();
        finish();
	}

	private String readFile(String fileName) throws MCFetchingException {
		
		Log.e("mcTest", "Read from: " + fileName);
		
		resources = getResources();
		
		int rID = resources.getIdentifier("com.mediacollector:raw/"+fileName, null, null);
		
		InputStream raw = getResources().openRawResource(rID);

	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	    int i;
	    try  {
	        i = raw.read();
	        while (i != -1) {
	            byteArrayOutputStream.write(i);
	            i = raw.read();
	        }
	        raw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
        return byteArrayOutputStream.toString(); 
	}
	
	private boolean writeFile(String fileContent, String fileName) {
        
		try {
			File root = Environment.getExternalStorageDirectory();
            if (root.canWrite()){
                File file = new File(root, fileName);
                FileWriter writer = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(writer);
                out.write(fileContent);
                out.close();
            }
         } catch (Exception ex) {
        	 Toast.makeText(this, ex.toString(), 
             		Toast.LENGTH_LONG).show();
         }
        return true;
    }
	
	public void fetch(String fileName) throws MCFetchingException {
	    
		String fileContent = readFile(fileName);
		
	    int count = 0;
	    
	    Matcher	matcher	= PATTERN_CODE.matcher(fileContent);
	    while(matcher.find()) {
	        count++;

	        Fetching fetch = new Fetching(this,matcher.group(1));
	        fetch.addObserver(this);
	        fetcher.add(fetch);
	        
	        
	        /*this.fetching = new Fetching(this, 
	        		matcher.group(1),Fetching.SEARCH_ENGINE_THALIA);
	        this.fetching.addObserver(this);
			this.fetching.fetchData();*/

			//Log.i("mcTest", String.valueOf(count) +": " + matcher.group(1));
	        //output += String.valueOf(count) +": " + matcher.group(1) +"\n";
			
	    }
	    

	}

	public void updateObserver(boolean statusOkay) {
		int counter = 1;
		while (counter < fetcher.size()) {
			try {
				this.fetcher.get(counter).fetchData();
			} catch (MCFetchingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    machfertig();
	}
	
	private boolean machfertig() {
		String fileName = "barcodes_books";
		String output = null;
		int counter = 1;
		while (counter < fetcher.size()) {
			Log.i("mcTest", String.valueOf(counter) +": " + this.fetcher.get(counter).toString());
			output += this.fetcher.get(counter).toString() + "\n";
		}
		
		if(writeFile(output,fileName)) {
	    	fileName += ".txt";
			Toast.makeText(this, "wrote " + fileName, 	
	        		Toast.LENGTH_LONG).show();
		}
		return true;
	}
}