package com.mediacollector;

import com.mediacollector.tools.RegisteredActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
		((LinearLayout) findViewById(R.id.back_to_start)).setOnClickListener(
				new OnClickListener() { public void onClick(View v) {
					startActivity(new Intent(EntryDetails.this, Start.class));			
					finish();
		}});
    }
}
