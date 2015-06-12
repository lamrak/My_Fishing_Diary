package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddNewFishing extends Activity {
	private EditText etPlace; 
	private EditText etDate;
	private EditText etWeather;
	private EditText etProcess;
	private EditText etCatch;
	private Button btnCreate;
	DB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fishing_list);

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
				String myDescription = etProcess.getText().toString();
				String myCatch = etCatch.getText().toString();
				
				FishingItem item = new FishingItem();
				item.setPlace(myPlace);
				item.setDate(myDate);
				item.setWeather(myWeather);
				item.setDescription(myDescription);
				item.setCatches(myCatch);
				//TODO other sets
				
				// open a connection to the database
				db = new DB(AddNewFishing.this);
				db.open();
				long id = db.saveFishingItem(item);
				db.close();

				Intent data = new Intent();
				FishingItem.packageIntent(data, myPlace, myDate, id);
				// send container
				setResult(RESULT_OK, data);
				finish();
			}
		});

	}


}
