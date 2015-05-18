package net.validcat.fishing;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddNewFishing extends Activity {
	private EditText etPlace, etDate, etWeather, etProcess, etCatch;
	private Button btnCreate;
	DB db;
	Cursor cursor;

	public SQLiteDatabase mDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fishing_list);

		// open a connection to the database
		db = new DB(this);
		mDB = db.open();

		// find items
		etPlace = (EditText) findViewById(R.id.etPlace);
		etDate = (EditText) findViewById(R.id.etDate);
		etWeather = (EditText) findViewById(R.id.etWeather);
		etProcess = (EditText) findViewById(R.id.etProcess);
		etCatch = (EditText) findViewById(R.id.etCatch);

		btnCreate = (Button) findViewById(R.id.btnCreate);

		// listener for the button
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// save in data base
				String myPlace = etPlace.getText().toString();
				String myDate = etDate.getText().toString();
				String myWeather = etWeather.getText().toString();
				String myProcess = etProcess.getText().toString();
				String myCatch = etCatch.getText().toString();				

				ContentValues cv = new ContentValues();
				cv.put(DB.COLUMN_PLACE, myPlace);
				cv.put(DB.COLUMN_DATE, myDate);
				cv.put(DB.COLUMN_WEATHER, myWeather);
				cv.put(DB.COLUMN_PROCESS, myProcess);
				cv.put(DB.COLUMN_CATCH, myCatch);
				 mDB.insert(DB.DB_TABLE, null, cv);
				// ID = db.getId();

			
				Intent data = new Intent();
				// put data
				FishingItem.packageIntent(data, getPlaceText(), getDateText());
				

				// send container
				setResult(RESULT_OK, data);
				finish();
				db.close();
			}
		});

	}

	// read the user-entered text
	private String getPlaceText() {
		return etPlace.getText().toString();
	}

	// read the user-entered text
	private String getDateText() {
		return etDate.getText().toString();
	}

}
