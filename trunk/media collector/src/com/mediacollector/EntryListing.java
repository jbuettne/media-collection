package com.mediacollector;

import java.util.ArrayList;

import com.mediacollector.collection.TextImageEntry;
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
public abstract class EntryListing extends RegisteredListActivity {
	
	protected ArrayList<TextImageEntry> entries = null;
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
        
        setListAdapter(new ArrayAdapter<TextImageEntry>(this, 
        		R.layout.group_row, entries) {
        	@Override
			public View getView(
					int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.entry_child_layout, null);
				}
				TextImageEntry o = entries.get(position);
				if (o != null) {
					((TextView) v.findViewById(R.id.name)).setText(o.getText());
					((TextView) v.findViewById(R.id.details)).setText(String
							.valueOf(o.getYear()));
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
        		entryDetails.putExtra("name", entries.get(position).getText());
        		entryDetails.putExtra("details", 
        				entries.get(position).getYear());
        		entryDetails.putExtra("extras", "");
        		entryDetails.putExtra("image", entries.get(position).getImage()
        				.toString());
        		entryDetails.putExtra("id", entries.get(position).getId());
        		startActivity(entryDetails);
        	}
        });
	}

}