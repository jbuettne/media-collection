package com.mediacollector.tools.Exceptions;

import android.content.Context;

public class MCSQLException extends MCException {

	/**
	 * Erkennungs-Tag für die Exception-Klasse.
	 */
	protected String excTag = "SQL";
	
	/**
	 * Generierte ID für das Serialisieren.
	 */
	private static final long serialVersionUID = -6442373107467832936L;
	
	/**
	 * Die Konstruktoren. Siehe hierfür die Elternklasse MCException.
	 */
	public MCSQLException(Context context) { super(context); }	
	public MCSQLException(Context context, String errorMsg) { 
		super(context, errorMsg); }	
	public MCSQLException(Context context, int status) {
		super(context, status); }	
	public MCSQLException(Context context, String errorMsg, int status) {
		super(context, errorMsg, status); }
	public MCSQLException(Context context, String errorMsg, int status,
			boolean log) { super(context, errorMsg, status, log); }

}
