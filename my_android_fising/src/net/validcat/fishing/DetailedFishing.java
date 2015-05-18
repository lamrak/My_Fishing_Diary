package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailedFishing extends Activity {
	TextView tvDetPlace, tvDetDate, tvDetWeather, tvDetProcess, tvDetCatch;
	int idIndex, placeIndex, dateIndex, weatherIndex, processIndex, catchIndex;
	String dataPlace, dataDate, dataWeather, dataProcess, dataCatch;
	public static final String LOG_TAG = "myLogs";
	DB db;
	Cursor cursor;
	int ID;
	SQLiteDatabase mDB;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_list);

		tvDetPlace = (TextView) findViewById(R.id.tvDetPlace);
		tvDetDate = (TextView) findViewById(R.id.tvDetDate);
		tvDetWeather = (TextView) findViewById(R.id.tvDetWeather);
		tvDetProcess = (TextView) findViewById(R.id.tvDetProcess);
		tvDetCatch = (TextView) findViewById(R.id.tvDetCatch);
		
		Intent intent = getIntent();
		ID = intent.getIntExtra("id",0);
		Log.d(LOG_TAG, " --- id Item ListView --- " + ID);
		
		

		db = new DB(this);
		db.open();
		
		String sqlQuerry = "SELECT * FROM mytab WHERE id = ‘ id ‘;)";

		cursor = mDB.rawQuery(sqlQuerry, null);

		if (cursor.moveToPosition(ID)) {

			idIndex = cursor.getColumnIndex(DB.COLUMN_ID);
			placeIndex = cursor.getColumnIndex(DB.COLUMN_PLACE);
			dateIndex = cursor.getColumnIndex(DB.COLUMN_DATE);
			weatherIndex = cursor.getColumnIndex(DB.COLUMN_WEATHER);
			processIndex = cursor.getColumnIndex(DB.COLUMN_PROCESS);
			catchIndex = cursor.getColumnIndex(DB.COLUMN_CATCH);

			dataPlace = cursor.getString(placeIndex);
			dataDate = cursor.getString(dateIndex);
			dataWeather = cursor.getString(weatherIndex);
			dataProcess = cursor.getString(processIndex);
			dataCatch = cursor.getString(catchIndex);

			tvDetPlace.setText("Место рыбалки: " + dataPlace);
			tvDetDate.setText("Дата :" + dataDate);
			tvDetWeather.setText("Погода: " + dataWeather);
			tvDetProcess.setText("Способ рыбалки: " + dataProcess);
			tvDetCatch.setText("Улов: " + dataCatch);

		} else
			Log.d(LOG_TAG, " --- 0 rows --- ");
		cursor.close();
		db.close();

		//
		// Intent intent = getIntent();
		// String myPlace = intent.getStringExtra("keyPlace");
		// String myDate = intent.getStringExtra("keyDate");
		// String myWeather = intent.getStringExtra("keyWeather");
		// String myProcess = intent.getStringExtra("keyProcess");
		// String myCatch = intent.getStringExtra("keyCatch");
		//
	}

}