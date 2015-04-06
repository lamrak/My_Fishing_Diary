package net.validcat.fishing;

import android.content.Intent;

public class Conteyner {

	// создаем константы
	public final static String PLACE = "place";
	public final static String DATE = "date";
	// создаем поля
	 String mPlace = new String();
	 String mDate = new String();

	 // конструктор
	public Conteyner(String place, String date) {
		// получаем данные из FishingList и инициализируем поля
		this.mPlace = place;
		this.mDate = date;
	}

	// конструктор
	public Conteyner(Intent intent) {
		// получаем данные из intent (пришол из MainActivity)
		mPlace = intent.getStringExtra(Conteyner.PLACE);
		mDate = intent.getStringExtra(Conteyner.DATE);
	}

	public static void packageIntent(Intent intent, String place, String date) {
		// Записываем данные в intent
		intent.putExtra(Conteyner.PLACE, place);
		intent.putExtra(Conteyner.DATE, date);
	}

	public String getPlace() {
		return mPlace;
	}
	
	public  String getDate() {
		return mDate;
	}
	


}
