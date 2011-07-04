package com.mediacollector;

import java.util.ArrayList;

import com.mediacollector.collection.Data;
import com.mediacollector.collection.books.Book;
import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.Preferences;
import com.mediacollector.tools.ScanBarcode;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
 * @author Philipp Dermitzel
 */
public abstract class SearchResult extends ListActivity {
	
	protected	  ArrayList<Data> 		searchResult 		= null;
	protected	  	String[] 			groups 		= null;
	protected	 	AlertDialog			alert		= null; 
	protected abstract void setData();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
		this.setData();
        setContentView(R.layout.search_result);      

        
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
        
        setListAdapter(new ArrayAdapter<Data>(this, 
        		R.layout.group_row, searchResult) {
        	@Override
			public View getView(
					int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.entry_child_layout, null);
				}
				Data o = searchResult.get(position);
				if (o != null) {
					((TextView) v.findViewById(R.id.name)).setText(o.name);
					((TextView) v.findViewById(R.id.details)).setText(String
							.valueOf(o.year));
					((ImageView) v.findViewById(R.id.image))
							.setImageDrawable((Drawable) getResources()
									.getDrawable(R.drawable.color_red));
				}
				return v;
			}
		});
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        			int position, long id) {
        		Intent entryDetails = new Intent(getBaseContext(),
        				EntryDetails.class);
        		entryDetails.putExtra("name", searchResult.get(position).name);
        		entryDetails.putExtra("details", String.valueOf(
        				searchResult.get(position).year));
        		entryDetails.putExtra("extra", searchResult.get(position).extra);
        		entryDetails.putExtra("id", searchResult.get(position).id);
        		startActivity(entryDetails);
        	}
        });
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