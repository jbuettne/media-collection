package com.mediacollector.sync;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.dropbox.client.DropboxAPI;
import com.dropbox.client.DropboxAPI.Config;
import com.dropbox.client.DropboxAPI.FileDownload;
import com.mediacollector.R;
import com.mediacollector.tools.Identifier;

/**
 * Klasse zur Synchronisation der Sammlungsdaten mit einer zentralen Dropbox.
 * Die Zugangsdaten werden unter den folgenden Konstanten abgelegt:
 * 	- APP_KEY			App Key
 * 	- APP_SECRET		Secret App Key
 * 	- LOGIN_EMAIL		Die E-Mail-Adresse des Dropbox-Account
 * 	- LOGIN_PASSWORD	Das Password zum Dropbx-Account
 * Weiterhin basieren Teile der Klasse grundlegend auf dem Beispielscript zur 
 * Dropbox-API: https://www.dropbox.com/developers
 * 
 * In der Dropbox wird durch die Programm die folgende Baumstruktur erzeugt:
 * /
 *    /identifier_1
 *       /identifier_1/changes
 *       /identifier_1/collections
 *    /identifier_2
 *    /identifier_3
 *    ...
 *    /identifier_n
 * Jeder "User" hat also einen eigenen Ordner mit seiner UUID als Namen. Darin
 * befinden sich zwei Dateien: changes und collections. collections beinhaltet
 * die gespeicherten Sammlungsdaten, changes den Timestamp der letzten 
 * Änderungen dieser Daten. Die Datei changes existiert als entsprechendes 
 * Gegenstück auch lokal auf dem mobilen Gerät.
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
	private static final String		LOGIN_PASSWORD		= "media1606collector";
	private static final String		LOGIN_EMAIL			= 
		"mediacollector@gmx.net";
    
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
    
    /**
     * Liefert die Dropbox-API-Instanz.
     * @return DropboxAPI Die Dropbox-API-Instanz.
     */
    public DropboxAPI getAPI() { 
    	return api; 
    }
    
    /**
     * Liefert die Dropbox-Config.
     * @return Config Die Dropbox-Config.
     */
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
    
    /**
     * Liefert die Schlüsselwerte aus den Preferences.
     * @return null/Array
     */
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
    
    /**
     * Setzt eine neue Config für Dropbox.
     * @param conf String Die zu setzende Config.
     */
    public void setConfig(Config conf) {
    	this.config = conf;
    }

    /**
     * Definiert, ob der Benutzer als eingeloggt gesehen wird oder nicht. Dies
     * ist NICHT der Login und hat auch nur UI-Zwecke.
     * @param loggedIn boolean
     */
    public void setLoggedIn(boolean loggedIn) {
    	this.loggedIn = loggedIn;
    }
    
    /***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
    
    /**
     * Der Standard-Konstruktor.
     * Auf Grund eines noch nicht genauer definierten Problems, wird im 
     * Augenblick bei jeder Instanziierung ein neuer Login durchgeführt.
     * Dies sollte in naher Zukunft verbessert werden!
     * @param context Der Context der aufrufenden Activity.
     */
    public Dropbox(final Context context) {
    	this.context = context;
    	/*if (getKeys() != null) setLoggedIn(true); else setLoggedIn(false);
        if (!this.loggedIn)
        	getAccountInfo();
        else setLoggedIn(true);*/
    	login(LOGIN_EMAIL, LOGIN_PASSWORD);
    }
    
    /***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
    
    /**
     * Speichert die Schlüsselwerte in den Preferences.
     * @param key String Der öffentliche Schlüsselwert.
     * @param secret String Der geheime Schlüsselwert.
     */
    public void storeKeys(String key, String secret) {
        SharedPreferences prefs = 
        	this.context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }
    
    /**
     * Löscht die gespeicherten Werte in den Preferences.
     */
    public void clearKeys() {
        SharedPreferences prefs = 
        	this.context.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
    
    /**
     * Führt den eigentlichen Login aus.
     */
    private void getAccountInfo() {
    	if (api.isAuthenticated()) login(null, null);
    	else login(LOGIN_EMAIL, LOGIN_PASSWORD);
    }

    /**
     * Authentifiziert den Benutzer. Hierüber kann - verlässlich - geprüft 
     * werden, ob der User eingeloggt ist.
     * @return boolean Eingeloggt ja/nein
     */
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
    
    /**
     * Führt den Login aus.
     * @param email Die E-Mail-Adresse zum Userkonto.
     * @param password Das Passwort zum Userkonto.
     */
    private void login(final String email, final String password) {
    	this.config = this.api.authenticate(getConfig(), 
    			email, password);
    	setConfig(this.config);
    	if (this.config != null 
    			&& this.config.authStatus == DropboxAPI.STATUS_SUCCESS) {
        	storeKeys(this.config.accessTokenKey, 
        			this.config.accessTokenSecret);
        	setLoggedIn(true);
        }
    }

    /**
     * Führt eine Synchronisation der Daten zwischen lokalem Speicher und der 
     * Dropbox aus. Dabei werden veraltete Daten ersetzt!
     * @throws IOException
     */
    public void sync() 
    throws IOException  {
    	File changes 		= new File(this.context.getFilesDir() 
    			+ FILE_CHANGES);
    	File collections 	= new File(this.context.getFilesDir() 
    			+ FILE_COLLECTIONS);
    	if (!changes.exists() || !collections.exists()) {
    		// Erste Synchronisation. Hier müssten dann auch noch die 
    		// entsprechenden Verzeichnisse in der DB erzeugt werden.
    		showToast("Erstes Mal...");
    		this.createLocalFiles();
    	}
    	BufferedReader reader = new BufferedReader(new FileReader(changes));
    	long timestampLocal 	= new Long(reader.readLine());
    	long timestampRemote	= this.getRemoteTimestamp();      	
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
    
    /**
     * Liefert den Timestamp der letzten Änderungen der Daten in der Dropbox.
     * @return long Timestamp der letzten Änderungen der Daten in der Dropbox.
     * @throws IOException
     */
    private Long getRemoteTimestamp() 
    throws IOException {
    	File changesTmp = new File(this.context.getFilesDir() + FILE_CHANGES 
    			+ "_tmp");
    	this.downloadFile("/" + Identifier.getIdentifier(this.context) + "/" 
    			+ FILE_CHANGES, changesTmp);
    	BufferedReader reader = new BufferedReader(new FileReader(changesTmp));
    	return new Long(reader.readLine());
    }
    
    /**
     * Läd eine Datei aus der Dropbox herunter und speichert den Inhalt im 
     * übergebenen File-Objekt.
     * @param remotePath String Der Pfad zur Datei in der Dropbox
     * @param localFile File Das File-Objekt, in welchem die Daten gespeichert
     * 					werden sollen
     * @throws IOException
     */
    private void downloadFile(String remotePath, File localFile) 
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
    }
    
    /**
     * Erstellt die lokalen Files bei der ersten Synchronisation. 
     * ACHTUNG: Dies sollte nicht bei der ersten Synchronisation, sondern beim 
     * ersten Start erfolgen! Dafür kann auch ein Context übergeben werden.
     * @throws IOException
     */
	private static void createLocalFiles(Context context) 
    throws IOException {
    	final File o = new File(context.getFilesDir() + FILE_COLLECTIONS);
    	final File h = new File(context.getFilesDir() + FILE_CHANGES);    	
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
    
    /**
     * Erstellt die lokalen Files bei der ersten Synchronisation. 
     * ACHTUNG: Dies sollte nicht bei der ersten Synchronisation, sondern beim 
     * ersten Start erfolgen! Dafür kann auch ein Context übergeben werden.
     * @throws IOException
     */
    private void createLocalFiles() 
    throws IOException {
    	createLocalFiles(this.context);
    }
    
    /**
     * Reine Hilfsmethode für das Debugging. Wird für das Release entfernt.
     * @param msg String
     */
    public void showToast(String msg) {
    	(Toast.makeText(this.context, msg, Toast.LENGTH_LONG)).show();
    }
    
}