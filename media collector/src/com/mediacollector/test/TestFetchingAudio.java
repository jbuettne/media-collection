package com.mediacollector.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.mediacollector.fetching.Fetching;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.Exceptions.MCFetchingException;

/**
 * Für David zum Ausbauen... ;-)
 * @author Philipp Dermitzel
 */
public class TestFetchingAudio implements Observer {
	
	Context 			context		= null;
	ArrayList<String>	eanList 	= new ArrayList<String>();
	ArrayList<Fetching>	fetcherList = new ArrayList<Fetching>();
	int 				counter 	= 1;
	Fetching			curFetcher	= null;			
	
	public TestFetchingAudio(Context context) {
		this.context = context;
		this.eanList.add("4039053403328");
		this.eanList.add("075678924484");
		//...
		this.eanList.add("8712725721512");
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
			String filePath = this.context.getFilesDir() + "/TESTLOG";
			File logFile = new File(filePath);
			if (!logFile.exists()) logFile.createNewFile();			
			FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String writeString = this.curFetcher.getDataFetcher().getEAN() 
				+ ": \"" + this.curFetcher.getDataFetcher().get("title") + "\"";
			bw.write(writeString);
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
