package net.validcat.fishing;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

public class FishingItem {

	public final static String PLACE = "place";
	public final static String DATE = "date";
	public final static String ID = "id";
	int dbId;
	String dbPlace;
	String dbDate;
	long idLong;

	String mPlace = new String();
	String mDate = new String();

	// constructor bdObject
	public FishingItem(int id, String place, String date) {
		this.dbId = id;
		this.dbPlace = place;
		this.dbDate = date;
	}

	// constructor
	public FishingItem(String place, String date) {

		// We obtain data in FishingList and initialize the field
		this.mPlace = place;
		this.mDate = date;
	}

	// constructor
	public FishingItem(Intent intent) {
		// obtain data in intent (come in MainActivity)
		dbPlace = intent.getStringExtra(FishingItem.PLACE);
		dbDate = intent.getStringExtra(FishingItem.DATE);
		idLong = intent.getLongExtra(FishingItem.ID, idLong);
	}

	public static void packageIntent(Intent data, String place, String date,
			long idLong) {
		// Save data in intent
		// FishingItem.PLACE - key
		data.putExtra(FishingItem.PLACE, place);
		data.putExtra(FishingItem.DATE, date);
		data.putExtra(FishingItem.ID, idLong);

	}

	// data which are set in the formation of a list item
	public String getPlace() {
		// return mPlace;
		return dbPlace;
	}

	public String getDate() {
		// return mDate;
		return dbDate;
	}

	// id BD
	public int getId() {
		return dbId;
	}

	// id new point
	public long getLongId() {
		return idLong;
	}

}
