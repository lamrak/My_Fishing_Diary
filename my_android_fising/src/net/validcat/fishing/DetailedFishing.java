package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailedFishing extends Activity {
	TextView tvDetWeather;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_list);

		tvDetWeather = (TextView) findViewById(R.id.tvDetWeather);
		// Intent intent = getIntent();
		// String myWeather = intent.getStringExtra("keyWeather");
		// tvDetWeather.setText("Погода: " + myWeather);
	}

}