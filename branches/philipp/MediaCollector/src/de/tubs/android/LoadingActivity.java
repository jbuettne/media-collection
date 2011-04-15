package de.tubs.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class LoadingActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        TextView legalNotes = (TextView) findViewById(R.id.legalNotes);
        legalNotes.setText("Media Collector (c) 2011\nfor more Informations "
        		+ "visit\ncode.google.com/p/media-collection");        
        new Thread(loadingThread).start();        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	goOn();
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    
    private void goOn() {
    	startActivity(new Intent(LoadingActivity.this, de.tubs.android.
    			StartActivity.class));
    }
    
    private Runnable loadingThread = new Runnable() {
    	public void run() {
    		try {
				Thread.sleep(3000);
				goOn();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    };

}