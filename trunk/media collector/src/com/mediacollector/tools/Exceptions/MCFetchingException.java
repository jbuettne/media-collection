package com.mediacollector.tools.Exceptions;

import android.content.Context;

public class MCFetchingException extends MCException {

	/**
	 * Erkennungs-Tag für die Exception-Klasse.
	 */
	protected String excTag = "FETCHING";
	
	/**
	 * Generierte ID für das Serialisieren.
	 */
	private static final long serialVersionUID = -2145338156400091160L;
	
	/**
	 * Die Konstruktoren. Siehe hierfür die Elternklasse MCException.
	 */
	public MCFetchingException(final String errorMsg) { super(errorMsg); }
	public MCFetchingException(final Context context) { super(context); }	
	public MCFetchingException(final Context context, final String errorMsg) { 
		super(context, errorMsg); }	
	public MCFetchingException(final Context context, final int status) {
		super(context, status); }	
	public MCFetchingException(final Context context, final String errorMsg, 
			final int status) { super(context, errorMsg, status); }
	public MCFetchingException(final Context context, final String errorMsg, 
			final int status, final boolean log) { 
		super(context, errorMsg, status, log); }

}
