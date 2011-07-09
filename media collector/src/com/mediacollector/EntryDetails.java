package com.mediacollector;

import com.mediacollector.tools.RegisteredActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryDetails extends RegisteredActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_details); 
        
        final Bundle extras = getIntent().getExtras();         
		((TextView) findViewById(R.id.artist)).setText(
				extras.getString("extra"));		
		((TextView) findViewById(R.id.release)).setText(
				extras.getString("name"));		
		((TextView) findViewById(R.id.year)).setText(
				extras.getString("details"));			
		((ImageView) findViewById(R.id.cover)).setImageDrawable((Drawable) 
				getResources().getDrawable(R.drawable.color_red));
        
    }
}
