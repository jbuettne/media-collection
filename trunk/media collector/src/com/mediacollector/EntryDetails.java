package com.mediacollector;

import java.io.File;

import com.mediacollector.tools.RegisteredActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Zeigt die Details eines gespeicherten Eintrages.
 * @author Philipp Dermitzel
 * @author Jens Büttner
 */
public class EntryDetails extends RegisteredActivity {
	Bitmap bitmap;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_details); 
        
        final Bundle extras = getIntent().getExtras();         
		TextView artist = (TextView) findViewById(R.id.artist);
		artist.setSelected(true);
		if (extras.getString("extra") == null || 
				extras.getString("extra").equals("")) {
			artist.setText(extras.getString("details"));
			((TextView) findViewById(R.id.year)).setText("");
		} else {
			artist.setText(extras.getString("extra"));
			((TextView) findViewById(R.id.year)).setText(
					extras.getString("details"));
		}
		TextView release = (TextView) findViewById(R.id.release);
		release.setSelected(true);
		release.setText(extras.getString("name"));		
					
		if (extras.getString("image") == null) 
			bitmap = BitmapFactory.decodeResource(getResources(), 
					R.drawable.no_cover);
		else {
			String path = extras.getString("image") + ".jpg";
			File bmFile = new File(path);
			if (bmFile.exists()) 
				bitmap = BitmapFactory.decodeFile(path);
			else bitmap = BitmapFactory.decodeResource(getResources(), 
					R.drawable.no_cover);
		}
					
		((ImageView) findViewById(R.id.cover)).setImageBitmap(bitmap);	
		((LinearLayout) findViewById(R.id.back_to_start)).setOnClickListener(
				new OnClickListener() { 
					public void onClick(View v) { finish(); }
				});
    }
}
