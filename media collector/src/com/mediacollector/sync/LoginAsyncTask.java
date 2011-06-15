package com.mediacollector.sync;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client.DropboxAPI;

public class LoginAsyncTask extends AsyncTask<Void, Void, Integer> {

    String user;
    String password;
    String errorMessage="";
    Dropbox dropboxActivity;
    
    DropboxAPI.Config config;
    DropboxAPI.Account account;
    
    public LoginAsyncTask(Dropbox act, 
    					  String user, 
    					  String password, 
    					  DropboxAPI.Config config) {
        super();
        this.dropboxActivity = act;
        this.user = user;
        this.password = password;
        this.config = config;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
        	DropboxAPI api = this.dropboxActivity.getAPI();        	
        	int success = DropboxAPI.STATUS_NONE;
        	if (!api.isAuthenticated()) {
	            this.config = api.authenticate(this.config, 
	            							   this.user, 
	            							   this.password);
	            this.dropboxActivity.setConfig(this.config);            
	            success = this.config.authStatus;
	            if (success != DropboxAPI.STATUS_SUCCESS) return success;
        	}
        	this.account = api.accountInfo();

        	if (!this.account.isError()) return DropboxAPI.STATUS_SUCCESS;
        	else return DropboxAPI.STATUS_FAILURE;
        } catch (Exception e) {
        	this.dropboxActivity.showToast("ARSCHKACKE!");
            return DropboxAPI.STATUS_NETWORK_ERROR;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == DropboxAPI.STATUS_SUCCESS) {
        	if (this.config != null 
        			&& this.config.authStatus == DropboxAPI.STATUS_SUCCESS) {
            	this.dropboxActivity.storeKeys(this.config.accessTokenKey, 
            			this.config.accessTokenSecret);
            	this.dropboxActivity.setLoggedIn(true);
            	this.dropboxActivity.showToast("ALLES JUT!");
            }
        } else {
        	if (result == DropboxAPI.STATUS_NETWORK_ERROR) {
        		this.dropboxActivity.showToast("Network error: " 
        				+ this.config.authDetail);
        	} else this.dropboxActivity.showToast("Unsuccessful login.");
        }
    }

}
