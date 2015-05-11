package net.validcat.fishing;

import android.content.Intent;

public class Container {

	public final static String PLACE = "place";
	public final static String DATE = "date";

	String mPlace = new String();
	String mDate = new String();

	// designer
	public Container(String place, String date) {

		// We obtain data in FishingList and initialize the field
		this.mPlace = place;
		this.mDate = date;
	}

	// designer
	public Container(Intent intent) {
		// obtain data in intent (come in MainActivity)
		mPlace = intent.getStringExtra(Container.PLACE);
		mDate = intent.getStringExtra(Container.DATE);
	}

	public static void packageIntent(Intent intent, String place, String date) {
		// Save data in intent
		intent.putExtra(Container.PLACE, place);
		intent.putExtra(Container.DATE, date);
	}

	public String getPlace() {
		return mPlace;
	}

	public String getDate() {
		return mDate;
	}

}
