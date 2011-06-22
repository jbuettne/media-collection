package com.mediacollector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.mediacollector.fetching.Audio;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResult extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result);
        
        final Bundle extras = getIntent().getExtras();
        
        String barcode = null;
        
        if (extras != null) {
        	barcode = extras.getString("BARCODE");   	
        } else {
        	barcode ="5050749203229";
        }
        
    	final Audio audio = new Audio();
		audio.setBarcode(barcode);
		audio.searchAudio();
    	try {
			showScanresult(audio);
		} catch (IOException e) {
			// TODO Auto-generated catch block
            Toast toast = Toast.makeText(getBaseContext() , e.toString(), Toast.LENGTH_LONG);
        	toast.show();
		}
    }
	
	private void showScanresult(Audio scanresult) throws IOException {
		
		//final TextView txtBarcode = (TextView) findViewById(R.id.txt_barcode);
		//txtBarcode.setText(String.valueOf(scanresult.getBarcode()));
		
		final TextView txtInterpret = (TextView) findViewById(R.id.artist);
		txtInterpret.setText(scanresult.getArtist());
		
		final TextView txtRelease = (TextView) findViewById(R.id.release);
		txtRelease.setText(scanresult.getTitle());
		
		final TextView txtYear = (TextView) findViewById(R.id.year);
		txtYear.setText(String.valueOf(scanresult.getYear()));
			
		ImageView image = (ImageView) findViewById(R.id.cover);
		URL url = new URL(scanresult.getCoverUrl());
    	InputStream content = (InputStream)url.getContent();
    	Drawable d = Drawable.createFromStream(content , "src");  
    	image.setImageDrawable(d);
 
	}

}
