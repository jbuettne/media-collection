package com.mediacollector.tools;

import com.mediacollector.InfoPopUp;
import com.mediacollector.R;
import com.mediacollector.ScanBarcode;
import com.mediacollector.sync.SyncActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * 
 * @author Philipp Dermitzel
 */
public class RegisteredActivity extends Activity {
	
	protected AlertDialog alert;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegistry.registerActivity(this);
        
        final String[] collections = {
        		getString(R.string.COLLECTION_Audio), 
        		getString(R.string.COLLECTION_Video), 
        		getString(R.string.COLLECTION_Books), 
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
            			.COLLECTION_Games)) intent.putExtra("collection", 
            					R.string.COLLECTION_Games);
            	else 
            		intent.putExtra("collection", 
            				R.string.COLLECTION_Wishlist);
            	startActivity(intent);
            }
        });
        alert = builder.create();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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
