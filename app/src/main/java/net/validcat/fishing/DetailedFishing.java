package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedFishing extends Activity {
	public static final String LOG_TAG = "myLogs";
		
	TextView tvDetPlace; 
	TextView tvDetDate; 
	TextView tvDetWeather; 
	TextView tvDetDescription;
	TextView tvDetCatch;
	DB db;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_list);
		
		tvDetPlace = (TextView) findViewById(R.id.tvDetPlace);
		tvDetDate = (TextView) findViewById(R.id.tvDetDate);
		tvDetWeather = (TextView) findViewById(R.id.tvDetWeather);
		tvDetDescription = (TextView) findViewById(R.id.tvDetProcess);
		tvDetCatch = (TextView) findViewById(R.id.tvDetCatch);

		Intent intent = getIntent();
		long id = intent.getLongExtra("id", -1);
		if (id == -1) {
			Toast.makeText(this, "Wrong id", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
			
		db = new DB(this);
		db.open();
		FishingItem item = db.getFishingItemById(id);
		db.close();
		// cursor = mDB.rawQuery("SELECT * FROM mytab WHERE _id = 'ID'", null);
		// cursor = db.getAllData();

		tvDetPlace.setText("Место рыбалки: " + item.getPlace());
		tvDetDate.setText("Дата :" + item.getDate());
		tvDetWeather.setText("Погода: " + item.getWeather());
		tvDetDescription.setText("Способ рыбалки: " + item.getDescription());
		tvDetCatch.setText("Улов: " + item.getCatch());
		
//		tvDetDate.setText("Дата :" + dataDate);
//		tvDetWeather.setText("Погода: " + dataWeather);
//		tvDetProcess.setText("Способ рыбалки: " + dataProcess);
//		tvDetCatch.setText("Улов: " + dataCatch);
	}
}