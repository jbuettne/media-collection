package com.mediacollector.tools;

import java.util.ArrayList;

import android.app.Activity;

/**
 * 
 * @author Philipp Dermitzel
 */
public class ActivityRegistry {
	
	private static ArrayList<Activity> activities = new ArrayList<Activity>();	

	public static void registerActivity(final Activity activity) {
		activities.add(activity);
	}

	public static void closeAllActivities() {
		for (Object obj : activities) {
			Activity activitiy = (Activity) obj;
			activitiy.finish();
		}
		System.exit(0);
	}

}
