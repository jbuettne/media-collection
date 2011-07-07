package com.mediacollector.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
	
	private Fetching fetching;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Toast.makeText(this, "Test: wird gestartet", 	
        		Toast.LENGTH_LONG).show();

        for( String fileName : files ) {
	        try {
	        	readFile(fileName);
				//readFile("barcodes_books");
			} catch (MCFetchingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        Toast.makeText(this, "Test: Fertig ", 
        		Toast.LENGTH_LONG).show();
        finish();
	}

	private void readFile(String fileName) throws MCFetchingException {
		
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
	    
	    int count = 0;
	    String output = "";
	    
	    Matcher	matcher	= PATTERN_CODE.matcher(byteArrayOutputStream.toString());
	    while(matcher.find()) {
	        count++;
/*	        this.fetching = new Fetching(this, 
	        		matcher.group(1));
	        this.fetching.addObserver(this);
			this.fetching.fetchData();
			Log.i("mcTest", String.valueOf(count) +": " + matcher.group(1) + " " 
					+ this.fetching.getDataFetcher().get(DataFetcher.TITLE_STRING));
*/			Log.i("mcTest", String.valueOf(count) +": " + matcher.group(1));
	        output += String.valueOf(count) +": " + matcher.group(1) +"\n";
			
	    }
	    
	    if(writeFile(output,fileName + ".txt")) {
			//Log.i("mcTest", "wrote " + fileName + ".txt");
			Toast.makeText(this, "wrote " + fileName + ".txt", 	
	        		Toast.LENGTH_LONG).show();
		}
        
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

	public void updateObserver(boolean statusOkay) {
	}
}