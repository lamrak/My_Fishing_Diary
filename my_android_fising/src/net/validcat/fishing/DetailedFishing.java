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
	long idLong;
	SQLiteDatabase mDB;
	private String[] allColumns = { DB.COLUMN_ID, DB.COLUMN_PLACE,
			DB.COLUMN_DATE, DB.COLUMN_WEATHER, DB.COLUMN_PROCESS,
			DB.COLUMN_CATCH };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_list);

		tvDetPlace = (TextView) findViewById(R.id.tvDetPlace);
		tvDetDate = (TextView) findViewById(R.id.tvDetDate);
		tvDetWeather = (TextView) findViewById(R.id.tvDetWeather);
		tvDetProcess = (TextView) findViewById(R.id.tvDetProcess);
		tvDetCatch = (TextView) findViewById(R.id.tvDetCatch);

		Intent intent = getIntent();
		ID = intent.getIntExtra("id", 0);
		Log.d(LOG_TAG, " --- id Item ListView --- " + ID);
		idLong = intent.getLongExtra("iDLong", 0);
		Log.d(LOG_TAG, " --- iDLong Item ListView --- " + idLong);

		db = new DB(this);
		mDB = db.open();

		// cursor = mDB.rawQuery("SELECT * FROM mytab WHERE _id = 'ID'", null);
		// cursor = db.getAllData();
		if (ID > 0) {
			cursor = mDB.query(DB.DB_TABLE, allColumns, DB.COLUMN_ID + " = "
					+ ID, null, null, null, null);
		} else
			cursor = mDB.query(DB.DB_TABLE, allColumns, DB.COLUMN_ID + " = "
					+ idLong, null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				// if (cursor.moveToPosition(ID-1)) {

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
				Log.d(LOG_TAG, " --- row 0 --- ");
		} else
			Log.d(LOG_TAG, " --- cursor = null --- ");
		cursor.close();
		db.close();

	}
}