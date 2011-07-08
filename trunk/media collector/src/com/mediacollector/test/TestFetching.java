package com.mediacollector.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.Toast;

import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.Fetching;
import com.mediacollector.fetching.ImageFetcher;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.Exceptions.MCFetchingException;

/**
 * Testet die gesammelten EANs.
 * @author Philipp Dermitzel
 */
public class TestFetching implements Observer {
	
	public static final int TEST_AUDIO = 1;
	public static final int TEST_BOOKS = 2;
	public static final int TEST_GAMES = 3;
	public static final int TEST_VIDEO = 4;
	
	private ArrayList<String>	eanList 	= new ArrayList<String>();
	private ArrayList<Fetching>	fetcherList = new ArrayList<Fetching>();
	private int 				counter 	= 1;
	private Fetching			curFetcher	= null;
	private Context 			context		= null;
	
	@SuppressWarnings("serial")
	private HashMap<Integer, ArrayList<String>> tests = 
		new HashMap<Integer, ArrayList<String>>() {{
		put(1, new ArrayList<String>() {{
			add("barcodes_audio");
			add("" + Fetching.SEARCH_ENGINE_THALIA);
		}});
		put(2, new ArrayList<String>() {{
			add("barcodes_book");
			add("" + Fetching.SEARCH_ENGINE_THALIA);
		}});
		put(3, new ArrayList<String>() {{
			add("barcodes_games");
			add("" + Fetching.SEARCH_ENGINE_TAGTOAD);
		}});
		put(4, new ArrayList<String>() {{
			add("barcodes_video");
			add("" + Fetching.SEARCH_ENGINE_OFDB);
		}});
	}};
	
	public TestFetching(final Context context, final int testType) {
		this.context = context;
		String file = this.tests.get(testType).get(0);
		Resources resources = this.context.getResources();
		InputStream raw = resources.openRawResource(resources.getIdentifier(
				"com.mediacollector:raw/" + file, null, null));
		ByteArrayOutputStream oST = new ByteArrayOutputStream();
		int i;
		try {
			i = raw.read();
			while (i != -1) {
				oST.write(i);
				i = raw.read();
			}
			raw.close();
			Matcher	matcher	= Pattern.compile("([0-9]{7,14})[^ ]+").matcher(
					oST.toString());
			while(matcher.find()) {
				Fetching fTmp = new Fetching(context, matcher.group(1), 
						new Integer(this.tests.get(testType).get(1)));
				fTmp.addObserver(this);
				this.fetcherList.add(fTmp);
			}
		} catch (IOException e) {}
	}
	
	public void startTest() {
		for (String ean : this.eanList) {
			Fetching fTmp = new Fetching(this.context, ean, 
					Fetching.SEARCH_ENGINE_THALIA);
			fTmp.addObserver(this);
			fetcherList.add(fTmp);
		}
		Toast.makeText(this.context, "Starte Tests", Toast.LENGTH_LONG).show();
		this.startFetcher();
	}
	
	private void startFetcher() {		
		try {
			this.curFetcher = this.fetcherList.get(0);
			this.curFetcher.fetchData();			
		} catch (MCFetchingException e) {
			Toast.makeText(this.context, "Fehler F", Toast.LENGTH_LONG).show();
		}
	}

	public void updateObserver(boolean statusOkay) {
		try {
			String filePath = Environment.getExternalStorageDirectory() 
				+ "/MediaCollector/TESTLOG_Audio";
			File logFile = new File(filePath);
			if (!logFile.exists()) logFile.createNewFile();			
			FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String writeString = this.curFetcher.getDataFetcher().getEAN() 
				+ ": " + this.curFetcher.getDataFetcher().get(
				DataFetcher.ARTIST_STRING) + " - " + this.curFetcher.
				getDataFetcher().get(DataFetcher.TITLE_STRING) + " (" + this.
				curFetcher.getDataFetcher().get(DataFetcher.YEAR_STRING) + ")"
				+ " | " + this.curFetcher.getImageFetcher().get(ImageFetcher
				.COVER_STRING);
			bw.write(writeString + "\n");
			bw.close();		
		} catch (Exception e) {
			Toast.makeText(this.context, "Fehler S", Toast.LENGTH_LONG).show();
		}
		this.fetcherList.remove(0);
		if (this.fetcherList.size() > 0) {
			this.counter = this.counter + 1;
			this.startFetcher();
		} else {		
			Toast.makeText(this.context, "Alle Tests fertig", 
					Toast.LENGTH_LONG).show();
		}
	}

}
