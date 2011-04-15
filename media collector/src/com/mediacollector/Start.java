package com.mediacollector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Start extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);        
        LinearLayout addField = (LinearLayout) findViewById(R.id.addField);
        addField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast toast = Toast.makeText(Start.this, 
        				"You clicked \"Add new Media\"", Toast.LENGTH_SHORT);
            	toast.show();
            }
        });
        LinearLayout syncField = (LinearLayout) findViewById(R.id.syncField);
        syncField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast toast = Toast.makeText(Start.this, 
        				"You clicked \"Sync Collection\"", Toast.LENGTH_SHORT);
            	toast.show();
            }
        });
        LinearLayout browseAudioField = (LinearLayout) findViewById(R.id
        		.browseAudioField);
        browseAudioField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast toast = Toast.makeText(Start.this, 
        				"You clicked \"Browse Audio-Collection\"", 
        				Toast.LENGTH_SHORT);
            	toast.show();
            }
        });   
        LinearLayout browseVideoField = (LinearLayout) findViewById(R.id
        		.browseVideoField);
        browseVideoField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast toast = Toast.makeText(Start.this, 
        				"You clicked \"Browse Video-Collection\"", 
        				Toast.LENGTH_SHORT);
            	toast.show();
            }
        });   
        LinearLayout browseBooksField = (LinearLayout) findViewById(R.id
        		.browseBooksField);
        browseBooksField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast toast = Toast.makeText(Start.this, 
        				"You clicked \"Browse Books-Collection\"", 
        				Toast.LENGTH_SHORT);
            	toast.show();
            }
        });   
        LinearLayout browseGamesField = (LinearLayout) findViewById(R.id
        		.browseGamesField);
        browseGamesField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast toast = Toast.makeText(Start.this, 
        				"You clicked \"Browse Games-Collection\"", 
        				Toast.LENGTH_SHORT);
            	toast.show();
            }
        });  
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		EditText searchText = (EditText) findViewById(R.id.searchText);
        		Toast toast = Toast.makeText(Start.this, 
        				"You want to search for\n\"" + searchText.getText()
        				.toString() + "\"", 
        				Toast.LENGTH_SHORT);
            	toast.show();
            }
        });   
    }
    
}