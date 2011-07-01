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
	public MCSQLException(final String errorMsg) { super(errorMsg); }	
	public MCSQLException(final Context context) { super(context); }	
	public MCSQLException(final Context context, final String errorMsg) { 
		super(context, errorMsg); }	
	public MCSQLException(final Context context, final int status) {
		super(context, status); }	
	public MCSQLException(final Context context, final String errorMsg, 
			final int status) { super(context, errorMsg, status); }
	public MCSQLException(final Context context, final String errorMsg, 
			final int status, final boolean log) { 
		super(context, errorMsg, status, log); }

}
