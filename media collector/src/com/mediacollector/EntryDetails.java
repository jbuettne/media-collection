package com.mediacollector;

import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.Preferences;
import com.mediacollector.tools.ScanBarcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryDetails extends Activity {
	
	protected	 AlertDialog			alert		= null; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_details); 
        
        final String[] collections = {
        		getString(R.string.COLLECTION_Audio), 
        		getString(R.string.COLLECTION_Video), 
        		getString(R.string.COLLECTION_Books),
        		getString(R.string.COLLECTION_Books_Man), 
        		getString(R.string.COLLECTION_Games), 
        		getString(R.string.COLLECTION_Wishlist) 
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.COLLECTION_Choose);
        builder.setItems(collections, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	Intent intent = new Intent(getApplicationContext(), 
            			ScanBarcode.class);
            	if (collections[item] == getString(R.string
            			.COLLECTION_Audio)) intent.putExtra("collection", 
            					R.string.COLLECTION_Audio);
            	else if (collections[item] == getString(R.string
            			.COLLECTION_Video)) intent.putExtra("collection", 
            					R.string.COLLECTION_Video);
            	else if (collections[item] == getString(R.string
            			.COLLECTION_Books)) intent.putExtra("collection", 
            					R.string.COLLECTION_Books);
            	else if (collections[item] == getString(R.string
            			.COLLECTION_Books_Man)) intent.putExtra("collection", 
            					R.string.COLLECTION_Books_Man);
            	else if (collections[item] == getString(R.string
            			.COLLECTION_Games)) intent.putExtra("collection", 
            					R.string.COLLECTION_Games);
            	else 
            		intent.putExtra("collection", 
            				R.string.COLLECTION_Wishlist);
            	startActivity(intent);
            }
        });
        alert = builder.create();
        
        final Bundle extras = getIntent().getExtras(); 
        
		final TextView txtInterpret = (TextView) findViewById(R.id.artist);
		txtInterpret.setText(extras.getString("extra"));
		
		final TextView txtRelease = (TextView) findViewById(R.id.release);
		txtRelease.setText(extras.getString("name"));
		
		final TextView txtYear = (TextView) findViewById(R.id.year);
		txtYear.setText(extras.getString("details"));
			
		ImageView image = (ImageView) findViewById(R.id.cover);
		image.setImageDrawable((Drawable) getResources()
				.getDrawable(R.drawable.color_red));
        
    }
	
	public void showEntryDetails() {
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }    
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.menu_sync:
			startActivity(new Intent(getBaseContext(), SyncActivity.class));
			return true;
		case R.id.menu_scan:
			alert.show();
			return true;
		case R.id.menu_settings:    			
			startActivity(new Intent(getBaseContext(), Preferences.class));
			return true;
		case R.id.menu_exit:
			ActivityRegistry.closeAllActivities();
			return true;
		case R.id.menu_info:    			
			startActivityForResult(new Intent(this, InfoPopUp.class), 1);
		default: return true;
    	}
    }

}
