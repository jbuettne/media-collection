package com.mediacollector;

import com.mediacollector.collection.Database;
import com.mediacollector.tools.RegisteredActivity;

import android.os.Bundle;
import android.os.Environment;

public class TestDBDelete extends RegisteredActivity {
	
	private Database dBase;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dBase = new Database(this);
        dBase.writeToCsv(Environment.getExternalStorageDirectory() 
				 + "/test.csv", "UTF-8");
        //dBase.readFromCsv(this.getFilesDir() + "/test.csv"); + "/MediaCollector/"
        //try {
//        artist = new ArtistData(this);
//        Artist art = new Artist();
//        art.name = "Fanta 007";
//            artist.deleteArtist(art.name, "name");
//        	showExDialog(art);
	}
    @Override
    protected void onDestroy() {
        dBase.closeConnection();
        super.onDestroy();
    }
}