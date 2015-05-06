package net.validcat.fishing;

import android.content.Intent;

public class Containe {

	public final static String PLACE = "place";
	public final static String DATE = "date";

	String mPlace = new String();
	String mDate = new String();

	// designer
	public Containe(String place, String date) {

		// We obtain data in FishingList and initialize the field
		this.mPlace = place;
		this.mDate = date;
	}

	// designer
	public Containe(Intent intent) {
		// obtain data in intent (come in MainActivity)
		mPlace = intent.getStringExtra(Containe.PLACE);
		mDate = intent.getStringExtra(Containe.DATE);
	}

	public static void packageIntent(Intent intent, String place, String date) {
		// Save data in intent
		intent.putExtra(Containe.PLACE, place);
		intent.putExtra(Containe.DATE, date);
	}

	public String getPlace() {
		return mPlace;
	}

	public String getDate() {
		return mDate;
	}

}
