package com.mediacollector;

import com.mediacollector.fetching.Fetching;

import android.app.Activity;
//import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;

public class ScanResult extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result);
        
        final Bundle extras = getIntent().getExtras();
  
        if (extras != null) {
        	
        	Toast toast = Toast.makeText(getBaseContext() , "lade daten", Toast.LENGTH_LONG);
        	toast.show();
        	new Fetching(this, 
        			extras.getString("BARCODE"), 
        			Fetching.SEARCH_ENGINE_TAGTOAD);
        
        }
        
//    	final Audio audio = new Audio();
//		audio.setBarcode(barcode);
//		audio.searchAudio();
//    	try {
//			showScanresult(audio);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//            Toast toast = Toast.makeText(getBaseContext() , e.toString(), Toast.LENGTH_LONG);
//        	toast.show();
//		}
    }
	
//	private void showScanresult(Audio scanresult) throws IOException {
//		
//		//final TextView txtBarcode = (TextView) findViewById(R.id.txt_barcode);
//		//txtBarcode.setText(String.valueOf(scanresult.getBarcode()));
//		
//		final TextView txtInterpret = (TextView) findViewById(R.id.artist);
//		txtInterpret.setText(scanresult.getArtist());
//		
//		final TextView txtRelease = (TextView) findViewById(R.id.release);
//		txtRelease.setText(scanresult.getTitle());
//		
//		final TextView txtYear = (TextView) findViewById(R.id.year);
//		txtYear.setText(String.valueOf(scanresult.getYear()));
//			
//		ImageView image = (ImageView) findViewById(R.id.cover);
//		URL url = new URL(scanresult.getCoverUrl());
//    	InputStream content = (InputStream)url.getContent();
//    	Drawable d = Drawable.createFromStream(content , "src");  
//    	image.setImageDrawable(d);
// 
//	}

}
