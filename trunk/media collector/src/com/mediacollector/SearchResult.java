package com.mediacollector;

import java.util.ArrayList;

import com.mediacollector.collection.Data;
import com.mediacollector.tools.ActivityRegistry;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Philipp Dermitzel
 */
public abstract class SearchResult extends ListActivity {
	
	protected	  ArrayList<Data> 		searchResult 		= null;
	protected abstract void setData();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
		this.setData();
        setContentView(R.layout.search_result);
        
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
        	  entryDetails.putExtra("details", String.valueOf(searchResult.get(position).year));
        	  entryDetails.putExtra("extra", searchResult.get(position).extra);
      		startActivity(entryDetails);
        	  //entryDetails.putExtra("image", (Drawable) getResources()
				//		.getDrawable(R.drawable.color_red));
            // When clicked, show a toast with the TextView text
//            Toast.makeText(getApplicationContext(), ((TextView)
//      			  view.findViewById(R.id.name)).getText(),
//                Toast.LENGTH_SHORT).show();
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