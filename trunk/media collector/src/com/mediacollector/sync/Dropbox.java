package com.mediacollector.sync;

import java.io.File;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

import com.dropbox.client.DropboxAPI;
import com.dropbox.client.DropboxAPI.Config;
import com.mediacollector.R;

/**
 * Modifiziertes Beispielprogramm zur Nutzung des Dropbox-SDKs.
 * Siehe hierzu: https://www.dropbox.com/developers
 * @author Philipp Dermitzel
 */
public class Dropbox extends Activity {
	
	/***************************************************************************
	 * KLASSENVARIABLEN
	 **************************************************************************/	
	
    final static private String 	APP_KEY 			= "dlbvko1demdc7ke";
    final static private String 	APP_SECRET 			= "16enl0keyoqj0ke";
    
    final static private String 	ACCOUNT_PREFS_NAME 	= "prefs";
    final static private String 	ACCESS_KEY_NAME 	= "ACCESS_KEY";
    final static private String 	ACCESS_SECRET_NAME 	= "ACCESS_SECRET";

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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getKeys() != null) setLoggedIn(true); else setLoggedIn(false);
        if (!this.loggedIn) {
        	final Bundle extras = getIntent().getExtras();
        	getAccountInfo(extras.getString("email"), 
        				   extras.getString("password"));
        } else setLoggedIn(true);
        uploadFile();
	}
    
    /***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/

    public void showToast(String msg) {
    	(Toast.makeText(this, msg, Toast.LENGTH_LONG)).show();
    }
    
    public String[] getKeys() {
    	SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
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
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }
    
    public void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
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
    	showToast(getString(R.string.DROPBOX_cnc));
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
    
    private void uploadFile() {
    	File moep = new File("moep.txt");
    	//try {
    		this.api.createFolder("dropbox", "testingtesting");
    		//this.api.getFile("dropbox", "alletjut.txt", moep, );
    	/*} catch (Exception ex) {
    		showToast(ex.toString());
    	}*/
    }
    
}