package net.validcat.fishing;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddNewFishing extends Activity {
	private EditText etPlace, etDate, etWeather;
	private Button btnCreate;
	DB db;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fishing_list);

		// open a connection to the database
		db = new DB(this);
		db.open();

		// find items
		etPlace = (EditText) findViewById(R.id.etPlace);
		etDate = (EditText) findViewById(R.id.etDate);
		etWeather = (EditText) findViewById(R.id.etWeather);
		btnCreate = (Button) findViewById(R.id.btnCreate);

		//  listener for the button
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// save in data base
				String myWeather = etWeather.getText().toString();
				db.addRec(3, myWeather);

				// gat information
				Intent data = new Intent();
				Containe.packageIntent(data, getPlaceText(), getDateText());

				// send containe
				setResult(RESULT_OK, data);
				finish();
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
