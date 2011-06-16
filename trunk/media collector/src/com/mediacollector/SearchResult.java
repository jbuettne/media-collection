package com.mediacollector;

import java.util.ArrayList;

import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.RegisteredActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Philipp Dermitzel
 */
public abstract class SearchResult extends ListActivity {
	
	protected	  ArrayList<String> 		searchResult 		= null;
	protected abstract void setData();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
		this.setData();
        setContentView(R.layout.search_result);
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.group_row,
        		searchResult));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
            // When clicked, show a toast with the TextView text
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
          }
        });
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
    			ActivityRegistry.closeAllActivities();
    			return true;
    		case R.id.menu_info:    			
    			startActivityForResult(new Intent(this, InfoPopUp.class), 1);
    		default: return true;
    	}
    }

}