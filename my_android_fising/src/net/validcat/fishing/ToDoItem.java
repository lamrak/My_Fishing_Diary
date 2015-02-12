package net.validcat.fishing;

import android.content.Intent;

public class ToDoItem {
	// не пойму для чего?
	public static final String ITEM_SEP = System.getProperty("line.separator");
	// создаем константы
	public final static String PLACE = "place";
	public final static String DATE = "date";
	// создаем поля
	 String mPlace = new String();
	 String mDate = new String();

	public ToDoItem(String place, String date) {
		// получаем данные из FishingList и инициализируем поля
		this.mPlace = place;
		this.mDate = date;
	}

	public ToDoItem(Intent intent) {
		// получаем данные из intent (пришол из MainActivity)
		mPlace = intent.getStringExtra(ToDoItem.PLACE);
		mDate = intent.getStringExtra(ToDoItem.DATE);

	}

	public static void packageIntent(Intent intent, String place, String date) {
		// Записываем данные в intent
		intent.putExtra(ToDoItem.PLACE, place);
		intent.putExtra(ToDoItem.DATE, date);
	}

	public String getPlace() {
		return mPlace;
	}
	
	public  String getDate() {
		return mDate;
	}
	
	// не пойму для чего?
	public String toString(){
		return mPlace + ITEM_SEP + mDate + ITEM_SEP;
	}

}
