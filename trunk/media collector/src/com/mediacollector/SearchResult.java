package com.mediacollector;

import java.util.ArrayList;

import com.mediacollector.collection.Data;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.RegisteredListActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Philipp Dermitzel
 */
public abstract class SearchResult extends RegisteredListActivity {
	
	protected ArrayList<Data>searchResult = null;
	protected String[] groups = null;
	
	protected RelativeLayout		more		= null;
	protected EditText filterText = null;
	protected ResultsAdapter adapter = null;
	
	protected abstract void setData();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
		this.setData();
        setContentView(R.layout.search_result); 
        
        this.more = (RelativeLayout) findViewById(R.id.more);
//        this.more.setOnClickListener(new OnClickListener() {            
//            public void onClick(View arg0) { hideHeader(); } 
//        });
        
        LinearLayout header = (LinearLayout) findViewById(R.id.overall_header);        
        header.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}        	
        });
        
        filterText = (EditText) findViewById(R.id.filterText);
        //filterText.addTextChangedListener(filterTextWatcher);   

		//adapter = new ResultsAdapter(this, R.layout.group_row, searchResult);
        setListAdapter(adapter);        
        
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        			int position, long id) {
        		Intent entryDetails = new Intent(getBaseContext(),
        				EntryDetails.class);
        		entryDetails.putExtra("name", searchResult.get(position).name);
        		entryDetails.putExtra("details", searchResult.get(position).year);
        		entryDetails.putExtra("image", searchResult.get(position).image);
        		entryDetails.putExtra("extra", searchResult.get(position).extra);
        		entryDetails.putExtra("id", searchResult.get(position).id);
        		startActivity(entryDetails);
        	}
        });
		registerForContextMenu(getListView());
    }
	
}