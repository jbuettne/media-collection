package com.mediacollector.sync;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client.DropboxAPI;
import com.dropbox.client.DropboxAPI.Config;
import com.dropbox.client.DropboxAPI.FileDownload;
import com.mediacollector.R;
import com.mediacollector.tools.Identifier;

/**
 * Modifiziertes Beispielprogramm zur Nutzung des Dropbox-SDKs.
 * Siehe hierzu: https://www.dropbox.com/developers
 * @author Philipp Dermitzel
 */
public class Dropbox {
	
	/***************************************************************************
	 * KLASSENVARIABLEN
	 **************************************************************************/	
	
	public static final	 String		FILE_CHANGES		= "changes";
	public static final  String		FILE_COLLECTIONS	= "collections";
	
	private static final String 	APP_KEY 			= "dlbvko1demdc7ke";
	private static final String 	APP_SECRET 			= "16enl0keyoqj0ke";
    
	private final static String 	ACCOUNT_PREFS_NAME 	= "prefs";
	private final static String 	ACCESS_KEY_NAME 	= "ACCESS_KEY";
	private final static String 	ACCESS_SECRET_NAME 	= "ACCESS_SECRET";

    private 			 Context	context				= null;
    private 			 DropboxAPI	api					= new DropboxAPI();    
    private 			 boolean 	loggedIn			= false;
    private 			 Config 	config				= null;
    
    /***************************************************************************
	 * Getter und Setter
	 **************************************************************************/
    
    public DropboxAPI getAPI() { 
    	return api; 
    }
    
    protected Config getConfig() {
    	if (this.config == null) {
    		this.config = api.getConfig(null, false);
    		this.config.consumerKey		= APP_KEY;
    		this.config.consumerSecret	= APP_SECRET;
    		this.config.server			= "api.dropbox.com";
    		this.config.contentServer	= "api-content.dropbox.com";
    		this.config.port			= 80;
    	}
    	return this.config;
    }
    
    public void setConfig(Config conf) {
    	this.config = conf;
    }

    public void setLoggedIn(boolean loggedIn) {
    	this.loggedIn = loggedIn;
    }
    
    /***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
    
    public Dropbox(	final Context context, 
    				final String email, 
    				final String password) {
    	this.context = context;
    	/*if (getKeys() != null) setLoggedIn(true); else */setLoggedIn(false);
        /*if (!this.loggedIn)*/
        	getAccountInfo(email, password);
        //else setLoggedIn(true);
    }
    
    /***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/

    public void showToast(String msg) {
    	(Toast.makeText(this.context, msg, Toast.LENGTH_LONG)).show();
    }
    
    public String[] getKeys() {
    	SharedPreferences prefs = 
    		this.context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key 		= prefs.getString(ACCESS_KEY_NAME, null);
        String secret 	= prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
        	String[] ret = new String[2];
        	ret[0] = key;
        	ret[1] = secret;
        	return ret;
        } else return null;
    }    
    
    public void storeKeys(String key, String secret) {
        SharedPreferences prefs = 
        	this.context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }
    
    public void clearKeys() {
        SharedPreferences prefs = 
        	this.context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
    
    private void getAccountInfo(final String email, final String password) {
    	if (api.isAuthenticated()) login(null, null);
    	else login(email, password);
    }

    protected boolean authenticate() {
    	if (this.config == null) this.config = getConfig();
    	String keys[] = getKeys();
    	if (keys != null) {
    		this.config = api.authenticateToken(keys[0], keys[1], this.config);
	        if (this.config != null) return true;
    	}
    	showToast(this.context.getString(R.string.DROPBOX_cnc));
    	clearKeys();
    	setLoggedIn(false);
    	return false;
    }
    
    private void login(final String email, final String password) {
    	this.config = this.api.authenticate(getConfig(), 
    			"mediacollector@gmx.net", "media1606collector");
    	setConfig(this.config);
    	if (this.config != null 
    			&& this.config.authStatus == DropboxAPI.STATUS_SUCCESS) {
        	storeKeys(this.config.accessTokenKey, 
        			this.config.accessTokenSecret);
        	setLoggedIn(true);
        } else showToast("Unsuccessful login.");
    }
    
    /***********
     * **********
     * @throws IOException
     */
    
    public void sync() 
    throws IOException  {
    	File changes 		= new File(this.context.getFilesDir() 
    			+ FILE_CHANGES);
    	File collections 	= new File(this.context.getFilesDir() 
    			+ FILE_COLLECTIONS);
    	if (!changes.exists() || !collections.exists()) {
    		showToast("Erstes Mal...");
    		this.createLocalFiles();
    	}
    	BufferedReader reader = new BufferedReader(new FileReader(changes));
    	int timestampLocal 	= new Integer(reader.readLine());
    	int timestampRemote	= this.getRemoteTimestamp();      	
    	if (timestampLocal == timestampRemote) {
    		showToast("Alles aktuell => keine Änderungen");
    	} else if (timestampLocal > timestampRemote) {
    		showToast("Lokale Änderungen => In die DB hochladen");
    	} else {
    		showToast("Änderungen in der Dropbox => updating...");
    		collections.delete();
    		this.downloadFile("/" + Identifier.getIdentifier(this.context) + "/" 
        			+ FILE_COLLECTIONS, collections);
    		changes.delete();
        	FileOutputStream fOSH = new FileOutputStream(changes);
        	fOSH.write(("" + timestampRemote).getBytes()); 
        	fOSH.flush();
        	fOSH.close();
    	}
    }
    
    private int getRemoteTimestamp() 
    throws IOException {
    	File changesTmp = new File(this.context.getFilesDir() + FILE_CHANGES 
    			+ "_tmp");
    	this.downloadFile("/" + Identifier.getIdentifier(this.context) + "/" 
    			+ FILE_CHANGES, changesTmp);
    	BufferedReader reader = new BufferedReader(new FileReader(changesTmp));
    	return new Integer(reader.readLine());
    }
    
    private boolean downloadFile(String remotePath, File localFile) 
    throws IOException{
    	BufferedInputStream 	br = null;
    	BufferedOutputStream 	bw = null;    	
    	try {
    		if (!localFile.exists()) localFile.createNewFile();
    		FileDownload fd = api.getFileStream("dropbox", remotePath, null);
    		br = new BufferedInputStream(fd.is);
    		bw = new BufferedOutputStream(new FileOutputStream(localFile));
    		byte[] buffer = new byte[4096];
    		int read;
    		while (true) {
    			read = br.read(buffer);
    			if (read <= 0) break;
    			bw.write(buffer, 0, read);
    		}
    	} finally {
    		if (bw != null) bw.close();
    		if (br != null) br.close();
    	}
    	return true;
    }
    
    private void createLocalFiles() 
    throws IOException {
    	final File o = new File(this.context.getFilesDir() + FILE_COLLECTIONS);
    	final File h = new File(this.context.getFilesDir() + FILE_CHANGES);    	
    	o.createNewFile();
    	h.createNewFile();
    	FileOutputStream fOSO = new FileOutputStream(o);
    	FileOutputStream fOSH = new FileOutputStream(h);
    	fOSO.write("DB-Data".getBytes()); // DB-Data...
    	fOSH.write(("" + (System.currentTimeMillis() / 1000)).getBytes());
    	fOSO.flush();
    	fOSH.flush();
    	fOSO.close();
    	fOSH.close();
    }
    
}