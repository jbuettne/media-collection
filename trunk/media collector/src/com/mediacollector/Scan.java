package com.mediacollector;

import java.io.IOException;

import com.mediacollector.collection.Database;
import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.Fetching;
import com.mediacollector.fetching.ImageFetcher;
import com.mediacollector.sync.Dropbox;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.RegisteredActivity;
import com.mediacollector.tools.ScanBarcode;
import com.mediacollector.tools.Exceptions.MCFetchingException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author David Pollehn
 * @author Philipp Dermitzel
 */
public class Scan extends RegisteredActivity implements Observer {
	
	@SuppressWarnings("unused")
	private static final ProgressDialog ProgressDialog = null;

	private Handler guiHandler = new Handler();
	
	private Fetching fetching;
	
	public int collection;
	
	private Database dBase = null;

	private static ProgressDialog progress;
	
	private String add2collection_values = "title";
	private boolean add2collection;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result);
        
        final Bundle extras = getIntent().getExtras();  
        if (extras != null) 
        	this.collection = extras.getInt("collection");
    	startActivityForResult(new Intent(this, ScanBarcode.class), 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, 
			final int resultCode, Intent data) {	
		switch (resultCode) {
			case Activity.RESULT_OK:
    			int searchEngine;
	        	switch (this.collection) {
	        		case R.string.COLLECTION_Audio:
	        			searchEngine = Fetching.SEARCH_ENGINE_THALIA_AUDIO;
	        			add2collection_values = "title+artist";
	        			break;
		        	case R.string.COLLECTION_Video:
		        		searchEngine = Fetching.SEARCH_ENGINE_OFDB;
		        		break;
		        	case R.string.COLLECTION_Books:
		        		searchEngine = Fetching.SEARCH_ENGINE_THALIA_BOOKS;
	        			add2collection_values = "title+artist";
		        		break;
		        	case R.string.COLLECTION_Games:
		        		if(data.getExtras().getString("FORMAT").contains("EAN"))
		        			searchEngine = Fetching.SEARCH_ENGINE_TAGTOAD;
		        		else
		        			searchEngine = Fetching.SEARCH_ENGINE_KAUFKAUF;
		        		break;
		        	default:
		        		searchEngine = Fetching.SEARCH_ENGINE_GOOGLE;
		        		break;
		        }
	        	try {
	        		progress = android.app.ProgressDialog.show(this, 
	        				getString(R.string.INFO_please_wait), 
	        				getString(R.string.INFO_we_comb_the_internet),
	        				true, true);
	        		this.fetching = new Fetching(this, data.getExtras()
	        				.getString("BARCODE"), searchEngine);
	        		this.fetching.addObserver(this);
        			this.fetching.fetchData();
	        	} catch (MCFetchingException e) {
					new MCFetchingException(this, e.getMessage());
				}
				break;
			case Activity.RESULT_CANCELED:
				finish();
				break;
			default: break;
		}
		
		LinearLayout backToStart = 
        	(LinearLayout) findViewById(R.id.back_to_start);
    	backToStart.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) { finish(); }
        });
    	
        super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void updateObserver(boolean statusOkay) {
		if (fetching.getDataFetcher().get(DataFetcher.TITLE_STRING) == null)
		{
			Toast.makeText(this, getString(R.string.INFO_nothing_found), 
					Toast.LENGTH_LONG).show();
			finish();
		}

		guiHandler.post(new Runnable() { public void run() {
			Bitmap coverBM;
			if ((String) fetching.getImageFetcher()
					.get(ImageFetcher.COVER_PATH)== null)
				coverBM = BitmapFactory.decodeResource(getResources(), 
						R.drawable.no_cover);
			else coverBM = BitmapFactory.decodeFile((String) fetching
						.getImageFetcher().get(ImageFetcher.COVER_PATH) 
						+ ".jpg");
			((ImageView) findViewById(R.id.cover)).setImageBitmap(coverBM);			
			TextView artist = (TextView) findViewById(R.id.artist);
			artist.setSelected(true);
			switch (collection) {
			case R.string.COLLECTION_Audio:
			case R.string.COLLECTION_Books:
				artist.setText((String) fetching.getDataFetcher().get(
						DataFetcher.ARTIST_STRING));
				((TextView) findViewById(R.id.year)).setText((String) 
						fetching.getDataFetcher().get(DataFetcher.YEAR_STRING));
				break;
			default:
				artist.setText((String) fetching.getDataFetcher().get(
						DataFetcher.YEAR_STRING));
				break;
			}
			TextView title = (TextView) findViewById(R.id.release);
			title.setSelected(true);
			title.setText((String) fetching.getDataFetcher().get(
					DataFetcher.TITLE_STRING));
			
			if(add2collection_values == "title+artist") {
				add2collection = fetching.getDataFetcher().get(
        				DataFetcher.TITLE_STRING) != null
        		&& fetching.getDataFetcher().get(
        				DataFetcher.ARTIST_STRING) != null;
			} else {
				add2collection = fetching.getDataFetcher().get(
        				DataFetcher.TITLE_STRING) != null;
			}
			
			if(add2collection) {
				
				LinearLayout addToCollection = 
		        	(LinearLayout) findViewById(R.id.add_to_collection);
				
				((TextView) addToCollection.findViewById(
						R.id.add_to_collection_text)).setText(
								R.string.SCAN_add);
				
		        addToCollection.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
				        dBase = new Database(getBaseContext());
							if (collection == 
								R.string.COLLECTION_Audio) {
								dBase.getArtist().insertArtist(
										(String) fetching.getDataFetcher().get(
												DataFetcher.ARTIST_ID_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.ARTIST_STRING));
								dBase.getAlbum().insertAlbum(
										(String) fetching.getDataFetcher().get(
												DataFetcher.TITLE_ID_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.TITLE_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.ARTIST_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.YEAR_STRING),
										(String) fetching.getImageFetcher().get(
												ImageFetcher.COVER_PATH),
										(String) fetching.getImageFetcher().get(
												ImageFetcher.COVER_STRING));
							} else if (collection ==
								R.string.COLLECTION_Books) {
								dBase.getBook().insertBook(
										(String) fetching.getDataFetcher().get(
												DataFetcher.TITLE_ID_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.TITLE_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.ARTIST_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.YEAR_STRING),
										(String) fetching.getImageFetcher().get(
												ImageFetcher.COVER_PATH),
										(String) fetching.getImageFetcher().get(
												ImageFetcher.COVER_STRING));
							} else if (collection == 
								R.string.COLLECTION_Video) {
								dBase.getFilm().insertFilm(
										(String) fetching.getDataFetcher().get(
												DataFetcher.TITLE_ID_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.TITLE_STRING),
										(String) fetching.getDataFetcher().get(
												DataFetcher.YEAR_STRING),
										(String) fetching.getImageFetcher().get(
												ImageFetcher.COVER_PATH),
										(String) fetching.getImageFetcher().get(
												ImageFetcher.COVER_STRING));
							} else if (collection == R.string.COLLECTION_Games) {
								if ((String) fetching.getDataFetcher()
										.get(DataFetcher.ARTIST_STRING) ==
											"Spiel") {
									dBase.getVideoGame().insertVideoGame(
											(String) fetching.getDataFetcher().get(
													DataFetcher.TITLE_ID_STRING),
											(String) fetching.getDataFetcher().get(
													DataFetcher.TITLE_STRING),
											(String) fetching.getDataFetcher().get(
													DataFetcher.YEAR_STRING),
											(String) fetching.getImageFetcher().get(
													ImageFetcher.COVER_PATH),
											(String) fetching.getImageFetcher().get(
													ImageFetcher.COVER_STRING));
								} else {
									dBase.getBoardGame().insertBoardGame(
											(String) fetching.getDataFetcher().get(
													DataFetcher.TITLE_ID_STRING),
											(String) fetching.getDataFetcher().get(
													DataFetcher.TITLE_STRING),
											(String) fetching.getDataFetcher().get(
													DataFetcher.YEAR_STRING),
											(String) fetching.getImageFetcher().get(
													ImageFetcher.COVER_PATH),
											(String) fetching.getImageFetcher().get(
													ImageFetcher.COVER_STRING));
								}
								
							}
							try {
								Dropbox.updateChangesTimestamp(
										getBaseContext());
							} catch (IOException e) {}
							finish();
						}
	        		});
				}
			}
		});
		progress.dismiss();
	}

    @Override
    protected void onDestroy() {
        if (dBase != null) dBase.closeConnection();
        super.onDestroy();
    }
}
