package com.mediacollector;

//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;

//import com.mediacollector.fetching.Audio;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;

public class EntryDetails extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry_details);
        
        final Bundle extras = getIntent().getExtras(); 
        
		final TextView txtInterpret = (TextView) findViewById(R.id.artist);
		txtInterpret.setText(extras.getString("extra"));
		
		final TextView txtRelease = (TextView) findViewById(R.id.release);
		txtRelease.setText(extras.getString("name"));
		
		final TextView txtYear = (TextView) findViewById(R.id.year);
		txtYear.setText(String.valueOf(extras.getLong("year")));
			
		ImageView image = (ImageView) findViewById(R.id.cover);
		image.setImageDrawable((Drawable) getResources()
				.getDrawable(R.drawable.color_red));
        
    }
	
	public void showEntryDetails() {
		
	}

}
