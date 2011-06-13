package com.mediacollector;

import android.app.Activity;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegistry.register(this);
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
    		case R.id.menu_scan:
    			startActivity(new Intent(getBaseContext(), ScanBarcode.class));
    			return true;
    		case R.id.menu_exit:
    			ActivityRegistry.closeAll();
    			return true;
    		case R.id.menu_info:    			
    			startActivityForResult(new Intent(this, InfoPopUp.class), 1);
    		default: return true;
    	}
    }

}
