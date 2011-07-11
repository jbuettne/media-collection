package com.mediacollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.games.VideoGameData;
import com.mediacollector.collection.games.listings.GamesListing;
import com.mediacollector.collection.video.FilmData;
import com.mediacollector.collection.video.listings.FilmListing;
import com.mediacollector.sync.Dropbox;
import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.Preferences;
import com.mediacollector.tools.RegisteredListActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.widget.Filter;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
public abstract class EntryListing extends RegisteredListActivity {
	
	protected ArrayList<TextImageEntry> entries = null;
	protected String[] groups = null;
	
	protected RelativeLayout		more		= null;
	protected EditText filterText = null;
	protected ArrayAdapter<TextImageEntry> adapter = null;
	
	protected abstract void setData();	

    protected final int TYPE_FILM = 3;
    protected final int TYPE_GAME = 4;
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
        
        this.more = (RelativeLayout) findViewById(R.id.more);
        this.more.setOnClickListener(new OnClickListener() {            
            public void onClick(View arg0) { hideHeader(); } 
        });
        
        LinearLayout header = (LinearLayout) findViewById(R.id.overall_header);        
        header.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}        	
        });
        
        filterText = (EditText) findViewById(R.id.filterText);
        filterText.addTextChangedListener(filterTextWatcher);   

        adapter = new ArrayAdapter<TextImageEntry>(this, 
        		R.layout.group_row, entries) {

            private ArrayFilter mFilter;
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
        	
        	@Override
            public Filter getFilter() {
                if (mFilter == null) {
                    mFilter = new ArrayFilter();
                }
                return mFilter;
            }      	
        	
		};
        setListAdapter(adapter);
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
		registerForContextMenu(getListView());
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
			case 3:	
				intent.putExtra("collection", R.string.COLLECTION_Video);
				startActivity(getIntent()); finish();
	        	break;
			case 4:	
				intent.putExtra("collection", R.string.COLLECTION_Games);
				startActivity(getIntent()); finish();
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
	
	private TextWatcher filterTextWatcher = new TextWatcher() {

	    public void afterTextChanged(Editable s) {
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	        adapter.getFilter().filter(s);
	    }

	};
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, 
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    		menu.setHeaderTitle(getString(R.string.MENU_Options));
    		menu.add(0, 1, 0, getString(R.string.MENU_Details));
    		menu.add(0, 2, 0, getString(R.string.MENU_Delete));
    }


    public boolean onContextItemSelected(MenuItem menuItem) {
    	AdapterContextMenuInfo info = 
        		(AdapterContextMenuInfo) menuItem.getMenuInfo();
    	switch (menuItem.getItemId()) {
	    	case 1: // Details
	    		Intent entryDetails = new Intent(this, EntryDetails.class);
				entryDetails.putExtra("name", entries.get(info.position)
						.getText());
				entryDetails.putExtra("details", entries.get(info.position)
						.getYear());
				entryDetails.putExtra("extra", "");
	    		entryDetails.putExtra("image", entries.get(info.position)
						.getImage());
				entryDetails.putExtra("id", entries.get(info.position)
						.getId());
				startActivity(entryDetails);
	    		return true;
	    	case 2: // Delete
	    		String entryID = entries.get(info.position).getId();
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
	    		try {
	    			Dropbox.updateChangesTimestamp(this);
	    		} catch (IOException e) {}
	    		return true;
	    	default: 
    		return super.onContextItemSelected(menuItem);
    	}
    }
    
    public void showHeader() {
        findViewById(R.id.overall_header_spacer).setVisibility(View.VISIBLE);
        findViewById(R.id.overall_header_container).setVisibility(View.VISIBLE);
        this.more.setOnClickListener(new OnClickListener() {
        	public void onClick(View arg0) { hideHeader(); }
        });
        ((ImageView) findViewById(R.id.more_img))
        	.setImageResource(R.drawable.less);
    }

    public void hideHeader() {
        findViewById(R.id.overall_header_spacer).setVisibility(View.GONE);
        findViewById(R.id.overall_header_container).setVisibility(View.GONE);
        this.more.setOnClickListener(new OnClickListener() {
        	public void onClick(View arg0) { showHeader(); }
        });
        ((ImageView) findViewById(R.id.more_img))
        	.setImageResource(R.drawable.more);
    }
    
    private class ArrayFilter extends android.widget.Filter {

        private List<TextImageEntry> mObjects;
        private final Object mLock = new Object();
        private ArrayList<TextImageEntry> mOriginalValues;
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<TextImageEntry>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<TextImageEntry> list = new ArrayList<TextImageEntry>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<TextImageEntry> values = mOriginalValues;
                final int count = values.size();

                final ArrayList<TextImageEntry> newValues = new ArrayList<TextImageEntry>(count);

                for (int i = 0; i < count; i++) {
                    final TextImageEntry value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].contains(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<TextImageEntry>) results.values;
            if (results.count > 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyDataSetInvalidated();
            }
        }
    }
}