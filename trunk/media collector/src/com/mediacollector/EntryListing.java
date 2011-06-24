package com.mediacollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.Preferences;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

/**
 *  
 * @author Philipp Dermitzel
 */
public abstract class EntryListing extends ExpandableListActivity {
	
	private final String 				TEXT	 	= "name";
	private final String 				IMAGE	 	= "image";
	private final String 				YEAR	 	= "year";
	private final String 				TRACKCOUNT	= "trackcount";
	
	protected	  String[] 				groups 		= null;
	protected 	  TextImageEntry[][] 	children 	= null;
	protected	  ImageView				more		= null;
	
	protected abstract void setData();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
		this.setData();
        setContentView(R.layout.entry_layout);
        
        LinearLayout header = (LinearLayout) findViewById(R.id.overall_header);        
        header.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(getBaseContext(), com.mediacollector
						.Start.class));
			}        	
        });
        final LayoutInflater layoutInflater = (LayoutInflater) 
        	this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        final ArrayList<HashMap<String, String>> groupData = 
        	new ArrayList<HashMap<String, String>>();
        final ArrayList<ArrayList<HashMap<String, Object>>> childData = 
        	new ArrayList<ArrayList<HashMap<String, Object>>>();
        
        for (Object obj : this.groups) {
        	HashMap<String, String> hmTmp = new HashMap<String, String>();
        	hmTmp.put(TEXT, obj.toString());
        	groupData.add(hmTmp);
        }
        for (Object obj : this.children) {
        	ArrayList<HashMap<String, Object>> alTmp = 
        		new ArrayList<HashMap<String, Object>>();
        	TextImageEntry[] curEntries = (TextImageEntry[]) obj;
        	for (Object objI : curEntries) {
        		HashMap<String, Object> hmTmp = new HashMap<String, Object>();
        		TextImageEntry curEntry = (TextImageEntry) objI;
        		hmTmp.put(TEXT, curEntry.getText());
        		hmTmp.put(IMAGE, curEntry.getImage());
        		hmTmp.put(YEAR, curEntry.getYear());
        		hmTmp.put(TRACKCOUNT, curEntry.getTrackCount());
        		alTmp.add(hmTmp);
        	}
        	childData.add(alTmp);
        }
        
        setListAdapter(new SimpleExpandableListAdapter(this, groupData, 
        		R.layout.group_row, new String[] { TEXT, IMAGE , YEAR,
        		TRACKCOUNT}, new int[] { R.id.groupname }, childData, 0, null, 
        		new int[] {}) {
        	@SuppressWarnings("unchecked")
			@Override
        	public View getChildView(int groupPosition, int childPosition, 
        			boolean isLastChild, View convertView, ViewGroup parent) {
        		final View v = super.getChildView(groupPosition, childPosition, 
        				isLastChild, convertView, parent);
        		((ImageView) v.findViewById(R.id.image)).setImageDrawable(
        				(Drawable) ((Map<String, Object>) 
        						getChild(groupPosition, childPosition))
        						.get(IMAGE));
        		((TextView) v.findViewById(R.id.name)).setText( 
        				(String) ((Map<String, Object>) 
        						getChild(groupPosition, childPosition))
        						.get(TEXT));
        		((TextView) v.findViewById(R.id.details)).setText( 
        				(String) ((Map<String, Object>) 
        						getChild(groupPosition, childPosition))
        						.get(YEAR) + "; " +
        				(String) ((Map<String, Object>) 
                				getChild(groupPosition, childPosition))
                				.get(TRACKCOUNT) + " Tracks");
        		return v;
        	}        	
        	public View newChildView(boolean isLastChild, ViewGroup parent) {
        		return layoutInflater.inflate(R.layout.entry_child_layout, 
        				null, false);
        	}
        });
        
		ExpandableListView list = (ExpandableListView) findViewById(
				android.R.id.list);
		list.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent entryDetails = new Intent(getBaseContext(),
						EntryDetails.class);
				entryDetails.putExtra("name",
						(String) childData.get(groupPosition)
								.get(childPosition).get(TEXT));
				entryDetails.putExtra("details",
						(String) childData.get(groupPosition)
								.get(childPosition).get(YEAR));
				entryDetails.putExtra("extra",
						(String) groupData.get(groupPosition).get(TEXT));
				startActivity(entryDetails);
				return true;
			}
		});
		registerForContextMenu(getExpandableListView());
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
			startActivity(new Intent(getBaseContext(), ScanBarcode.class));
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