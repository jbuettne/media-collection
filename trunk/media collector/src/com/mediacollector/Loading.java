package com.mediacollector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Loading extends Activity {
	
	boolean resume = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);        
        TextView legalNotes = (TextView) findViewById(R.id.legalNotes);
        legalNotes.setText("Media Collector (c) 2011\nfor more Informations "
        		+ "visit\ncode.google.com/p/media-collection");        
        new Thread(loadingThread).start();        
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (this.resume) {
    		startActivity(new Intent(Loading.this, com.mediacollector
    				.Start.class));
    	} 
    }
    
    private Runnable loadingThread = new Runnable() {
    	public void run() {
    		try {
				Thread.sleep(3000);
				resume = true;
				startActivity(new Intent(Loading.this, com.mediacollector
						.Start.class));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    };

}