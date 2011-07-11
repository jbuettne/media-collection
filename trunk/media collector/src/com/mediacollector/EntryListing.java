package com.mediacollector;

import java.util.ArrayList;

import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.games.VideoGameData;
import com.mediacollector.collection.games.listings.GamesListing;
import com.mediacollector.collection.video.FilmData;
import com.mediacollector.collection.video.listings.FilmListing;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.RegisteredListActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
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

    protected final int TYPE_FILM = 1;
    protected final int TYPE_GAME = 2;
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
					Bitmap cover;
					if (o.getImage() != null)
						cover = BitmapFactory.decodeFile(
								o.getImage() + "_small.jpg");
					else cover = BitmapFactory.decodeResource(
								getResources(),R.drawable.no_cover);
					((ImageView) v.findViewById(R.id.image))
							.setImageBitmap(cover);
						
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
        		entryDetails.putExtra("image", entries.get(position).getImage());
        		entryDetails.putExtra("id", entries.get(position).getId());
        		startActivity(entryDetails);
        	}
        });
	}   
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, 
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    		menu.setHeaderTitle(getString(R.string.MENU_Options));
    		menu.add(0, 1, 0, getString(R.string.MENU_Details));
    		menu.add(0, 2, 0, getString(R.string.MENU_Delete));
    }


    public boolean onContextItemSelected(MenuItem menuItem) {
    	int menuItemIndex = menuItem.getItemId();
    	switch (menuItem.getItemId()) {
	    	case 1: // Details
	    		Intent entryDetails = new Intent(this, EntryDetails.class);
				entryDetails.putExtra("name", entries.get(menuItemIndex)
						.getText());
				entryDetails.putExtra("details", entries.get(menuItemIndex)
						.getYear());
				entryDetails.putExtra("extra", "");
	    		entryDetails.putExtra("image", entries.get(menuItemIndex)
						.getImage());
				entryDetails.putExtra("id", entries.get(menuItemIndex)
						.getId());
				startActivity(entryDetails);
	    		return true;
	    	case 2: // Delete
	    		String entryID = entries.get(menuItemIndex).getId();
	    		switch (this.getType()) {
	    		case TYPE_FILM:
	    			FilmData curFilm = new FilmData(this);
	    			curFilm.deleteFilm(entryID);
	    			finish();
	    			startActivity(new Intent(getBaseContext(), 
	    					FilmListing.class));
	    			break;
	    		case TYPE_GAME:
	    			VideoGameData curGame = new VideoGameData(this);
	    			curGame.deleteVideoGame(entryID);
	    			finish();
	    			startActivity(new Intent(getBaseContext(), 
	    					GamesListing.class));
	    			break;
	    		}
	    		return true;
	    	default: 
    		return super.onContextItemSelected(menuItem);
    	}
    }
}