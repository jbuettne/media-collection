package com.mediacollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mediacollector.collection.TextImageEntry;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
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
	
	protected	  String[] 				groups 		= null;
	protected 	  TextImageEntry[][] 	children 	= null;
	protected	  ImageView				more		= null;
	
	protected abstract void setData();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
        		alTmp.add(hmTmp);
        	}
        	childData.add(alTmp);
        }
        
        setListAdapter(new SimpleExpandableListAdapter(this, groupData, 
        		R.layout.group_row, new String[] { TEXT, IMAGE }, 
        		new int[] { R.id.groupname }, childData, 0, null, 
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
        		return v;
        	}        	
        	public View newChildView(boolean isLastChild, ViewGroup parent) {
        		return layoutInflater.inflate(R.layout.entry_child_layout, 
        				null, false);
        	}
        });
        
        ExpandableListView list = (ExpandableListView) findViewById(
        		android.R.id.list);
        list.setOnChildClickListener(new OnChildClickListener(){
        	public boolean onChildClick(ExpandableListView parent, View v,
        			int groupPosition, int childPosition, long id) {
        		// tue etwas...
        		return true;
        	}
        });
		registerForContextMenu(getExpandableListView());
	}
	
}