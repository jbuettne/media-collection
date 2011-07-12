package com.mediacollector;

import java.io.IOException;
import java.util.ArrayList;

import com.mediacollector.collection.SearchListing;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.AlbumTbl;
import com.mediacollector.collection.books.BookData;
import com.mediacollector.collection.books.BookTbl;
import com.mediacollector.collection.games.VideoGameData;
import com.mediacollector.collection.games.VideoGameTbl;
import com.mediacollector.collection.games.listings.GamesListing;
import com.mediacollector.collection.video.FilmData;
import com.mediacollector.collection.video.FilmTbl;
import com.mediacollector.collection.video.listings.FilmListing;
import com.mediacollector.sync.Dropbox;
import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.ActivityRegistry;
import com.mediacollector.tools.Preferences;
import com.mediacollector.tools.RegisteredListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public abstract class EntryListing extends RegisteredListActivity {
	
	protected ArrayList<TextImageEntry> entries = null;
	protected String[] groups = null;
	
	protected RelativeLayout		more		= null;
	protected EditText filterText = null;
	protected ResultsAdapter adapter = null;
	protected Bundle extras = null;
	
	protected abstract void setData();	

    protected final int TYPE_SEARCH = -1;
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

		adapter = new ResultsAdapter(this, R.layout.group_row, entries);
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        			int position, long id) {
        		Intent entryDetails = new Intent(getBaseContext(),
        				EntryDetails.class);
				entryDetails.putExtra("name", adapter.getItem(position)
						.getText());
				entryDetails.putExtra("details", adapter.getItem(position)
						.getYear());
				entryDetails.putExtra("extras", adapter.getItem(position)
						.getExtra());
				entryDetails.putExtra("image", adapter.getItem(position)
						.getImage());
				entryDetails.putExtra("id", adapter.getItem(position).getId());
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
					startActivity(intent);
		        	break;
				case 4:	
					intent.putExtra("collection", R.string.COLLECTION_Games);
					startActivity(getIntent()); finish();
					startActivity(intent);
					break;
				default:
					alert.show();
					break;
			}
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
				entryDetails.putExtra("name", adapter.getItem(info.position)
						.getText());
				entryDetails.putExtra("details", adapter.getItem(info.position)
						.getYear());
				entryDetails.putExtra("extra", adapter.getItem(info.position)
						.getExtra());
	    		entryDetails.putExtra("image", adapter.getItem(info.position)
						.getImage());
				entryDetails.putExtra("id", adapter.getItem(info.position)
						.getId());
				startActivity(entryDetails);
	    		return true;
	    	case 2: // Delete
	    		String entryID = adapter.getItem(info.position).getId();
	    		switch (this.getType()) {
	    		case TYPE_SEARCH:
	    			Intent searchResult = new Intent(this, SearchListing.class);
	    			searchResult.putExtra("searchText", 
	    					extras.getString("searchText"));
	    			if (adapter.getItem(info.position)
	    					.getTable() == AlbumTbl.TABLE_NAME) {
	    				entryID = adapter.getItem(info.position).getText();
	    				AlbumData curAlbum = new AlbumData(this);
	    				curAlbum.deleteAlbumName(entryID);
	    				finish();
	    				curAlbum.close();
	    				startActivity(searchResult);
	    			} else if (adapter.getItem(info.position)
	    					.getTable() == BookTbl.TABLE_NAME) {
	    				BookData curBook = new BookData(this);
	    				curBook.deleteBook(entryID);
	    				finish();
	    				curBook.close();	
	    				startActivity(searchResult);			
	    			} else if (adapter.getItem(info.position)
	    					.getTable() == FilmTbl.TABLE_NAME){
	    				FilmData curFilm = new FilmData(this);
	    				curFilm.deleteFilm(entryID);
	    				finish();
	    				curFilm.close();	    
	    				startActivity(searchResult);			
	    			} else if (adapter.getItem(info.position)
	    					.getTable() == VideoGameTbl.TABLE_NAME){
	    				VideoGameData curGame = new VideoGameData(this);
	    				curGame.deleteVideoGame(entryID);
	    				finish();
	    				curGame.close();
	    				startActivity(searchResult);
	    			}
	    			break;
	    		case TYPE_FILM:
	    			FilmData curFilm = new FilmData(this);
	    			curFilm.deleteFilm(entryID);
	    			finish();
	    			startActivity(new Intent(getBaseContext(), 
	    					FilmListing.class));
	    			curFilm.close();
	    			break;
	    		case TYPE_GAME:
	    			VideoGameData curGame = new VideoGameData(this);
	    			curGame.deleteVideoGame(entryID);
	    			finish();
	    			startActivity(new Intent(getBaseContext(), 
	    					GamesListing.class));
	    			curGame.close();
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
}