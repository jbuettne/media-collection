package com.mediacollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.listings.ArtistListing;
import com.mediacollector.collection.books.BookData;
import com.mediacollector.collection.books.listings.BookListing;
import com.mediacollector.sync.Dropbox;
import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.Preferences;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

/**
 *  
 * @author Philipp Dermitzel
 */
public abstract class EntryListingExp extends ExpandableListActivity {    


	private final 	String 				ID	 		= "id";
	private final 	String 				TEXT	 	= "name";
	private final 	String 				IMAGE	 	= "image";
	private final 	String 				YEAR	 	= "year";
	private final 	String 				TRACKCOUNT	= "trackcount";
	
	protected	  	String[] 			groups 		= null;
	protected 	  	TextImageEntry[][] 	children 	= null;
	protected	  	ImageView			more		= null;
	protected	 	AlertDialog			alert		= null; 
	
	protected ArrayList<HashMap<String, String>> groupData = 
    	new ArrayList<HashMap<String, String>>();
    protected ArrayList<ArrayList<HashMap<String, Object>>> childData = 
    	new ArrayList<ArrayList<HashMap<String, Object>>>();
    
    protected final int TYPE_AUDIO = 1;
    protected final int TYPE_BOOKS = 2;
	
	protected abstract void setData();
	
	/**
	 * Liefert die Art des Listings. Die Arten des Listings sind aus den oben 
	 * aufgeführten Konstanten zu entnehmen.
	 * @return int
	 */
	protected abstract int getType();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
		this.setData();
        setContentView(R.layout.entry_layout);
        
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
            			Scan.class);
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
        
        LinearLayout header = (LinearLayout) findViewById(R.id.overall_header);        
        header.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}        	
        });
        final LayoutInflater layoutInflater = (LayoutInflater) 
        	this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
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
        		hmTmp.put(ID, curEntry.getId());
        		hmTmp.put(TEXT, curEntry.getText());
        		hmTmp.put(IMAGE, curEntry.getImage());
        		hmTmp.put(YEAR, curEntry.getYear());
        		//hmTmp.put(TRACKCOUNT, curEntry.getTrackCount());
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
        		((ImageView) v.findViewById(R.id.image))
        		.setImageBitmap(BitmapFactory.decodeFile(
        				(String) ((Map<String, Object>) 
        						getChild(groupPosition, childPosition))
        						.get(IMAGE) + "_small.jpg"));
        		((TextView) v.findViewById(R.id.name)).setText( 
        				(String) ((Map<String, Object>) 
        						getChild(groupPosition, childPosition))
        						.get(TEXT));
        		((TextView) v.findViewById(R.id.details)).setText( 
        				(String) ((Map<String, Object>) 
        						getChild(groupPosition, childPosition))
        						.get(YEAR));
//        						+ "; " +
//        				(String) ((Map<String, Object>) 
//                				getChild(groupPosition, childPosition))
//                				.get(TRACKCOUNT) + " Tracks");
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
        		entryDetails.putExtra("image", 
        				(String) childData.get(groupPosition)
						.get(childPosition).get(IMAGE));
				entryDetails.putExtra("id",
						(String) childData.get(groupPosition)
						.get(childPosition).get(ID));
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
			Intent intent = new Intent(getBaseContext(), Scan.class);
			Log.i("mcTest", String.valueOf(getType()));
			switch (getType()) {
			case 1:	intent.putExtra("collection", R.string.COLLECTION_Audio);
	        	break;
			case 2:	intent.putExtra("collection", R.string.COLLECTION_Books);
        		break;
			}
			startActivity(intent);
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
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, 
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);    	
    	ExpandableListContextMenuInfo info = 
    		(ExpandableListContextMenuInfo) menuInfo;
    	int type = 
    		ExpandableListView.getPackedPositionType(info.packedPosition);
    	if (type == 1) {
    		menu.setHeaderTitle(getString(R.string.MENU_Options));
    		menu.add(0, 1, 0, getString(R.string.MENU_Details));
    		menu.add(0, 2, 0, getString(R.string.MENU_Delete));
    	}
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
    	ExpandableListContextMenuInfo info = 
    		(ExpandableListContextMenuInfo) menuItem.getMenuInfo();
    	int groupPosition = 0, 
    		childPosition = 0;
    	if (ExpandableListView.getPackedPositionType(info.packedPosition) 
    			== ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
    		groupPosition = 
    			ExpandableListView.getPackedPositionGroup(info.packedPosition);
    		childPosition = 
    			ExpandableListView.getPackedPositionChild(info.packedPosition);
    	}    	
    	switch (menuItem.getItemId()) {
	    	case 1: // Details
	    		Intent entryDetails = new Intent(this, EntryDetails.class);
				entryDetails.putExtra("name", (String) childData.get(groupPosition)
						.get(childPosition).get(TEXT));
				entryDetails.putExtra("details", (String) childData
						.get(groupPosition).get(childPosition).get(YEAR));
				entryDetails.putExtra("extra", (String) groupData.get(groupPosition)
						.get(TEXT));
	    		entryDetails.putExtra("image", (String) childData.get(groupPosition)
						.get(childPosition).get(IMAGE));
				entryDetails.putExtra("id", (String) childData.get(groupPosition)
						.get(childPosition).get(ID));
				startActivity(entryDetails);
	    		return true;
	    	case 2: // Delete
	    		String entryID = (String) childData.get(groupPosition).get(
	    				childPosition).get(ID);
	    		switch (this.getType()) {
	    		case TYPE_AUDIO:
	    			AlbumData curAlbum = new AlbumData(this);
	    			curAlbum.deleteAlbum(entryID);
	    			finish();
	    			startActivity(new Intent(getBaseContext(), 
	    					ArtistListing.class));
	    			break;
	    		case TYPE_BOOKS:
	    			BookData curBook = new BookData(this);
	    			curBook.deleteBook(entryID);
	    			finish();
	    			startActivity(new Intent(getBaseContext(), 
	    					BookListing.class));
	    			break;
	    		}
	    		try {
	    			Dropbox.updateChangesTimestamp(this);
	    		} catch (IOException e) {}
	    		return true;
	    	default: 
    		return super.onContextItemSelected(menuItem);
    	}
    }
    	
}