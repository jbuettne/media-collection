package com.mediacollector;

import java.util.ArrayList;

import com.mediacollector.collection.Data;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.RegisteredListActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Philipp Dermitzel
 */
public abstract class SearchResult extends RegisteredListActivity {
	
	protected ArrayList<Data>searchResult = null;
	protected String[] groups = null;
	protected abstract void setData();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
		this.setData();
        setContentView(R.layout.search_result);    
        
        LinearLayout header = (LinearLayout) findViewById(R.id.overall_header);        
        header.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(getBaseContext(), com.mediacollector
						.Start.class));
			}        	
        });
        
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
					((TextView) v.findViewById(R.id.details)).setText(o.year);
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
	
}