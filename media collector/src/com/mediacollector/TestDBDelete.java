package com.mediacollector;

import com.mediacollector.collection.Database;
import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.tools.RegisteredActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class TestDBDelete extends RegisteredActivity {
	Database dBase;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dBase = new Database(this);
        dBase.writeCsv(this.getFilesDir() + "/test.csv", "UTF-8");
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