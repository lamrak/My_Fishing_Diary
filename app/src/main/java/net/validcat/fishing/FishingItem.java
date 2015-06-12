package net.validcat.fishing;

import android.content.Intent;

public class FishingItem {
	public final static String PLACE = "place";
	public final static String DATE = "date";
	public final static String ID = "id";
	
	long id;
	String place;
	String date; //Date
	//int weather 
	String weather;
	String description;
	String catches; 

	// constructor bdObject
	public FishingItem(int id, String place, String date) {
		this.id = id;
		this.place = place;
		this.date = date;
	}

	// constructor
	public FishingItem(String place, String date) {
		// We obtain data in FishingList and initialize the field
		this.place = place;
		this.date = date;
	}

	// constructor
	public FishingItem(Intent intent) {
		// obtain data in intent (come in MainActivity)
		place = intent.getStringExtra(FishingItem.PLACE);
		date = intent.getStringExtra(FishingItem.DATE);
		id = intent.getLongExtra(FishingItem.ID, -1L);
	}

	public FishingItem() {
		// TODO Auto-generated constructor stub
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
	public long getId() {
		return id;
	}
	
	public String getDate() {
		return date;
	}

	public String getPlace() {
		return place;
	}
	
	public String getWeather() {
		return weather;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCatch() {
		return catches;
	}	

	public void setPlace(String place) {
		this.place = place;
	}	

	public void setDate(String date) {
		this.date = date;	
	}

	public void setWeather(String weather) {
		this.weather = weather;		
	}

	public void setDescription(String description) {
		this.description = description;		
	}

	public void setCatches(String catches) {
		this.catches = catches;		
	}	

}
