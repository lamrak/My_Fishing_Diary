package net.validcat.fishing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailedFishing extends Activity {
	TextView tvDetPlace,tvDetDate,tvDetWeather,tvDetProcess,tvDetCatch;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_list);
		
		tvDetPlace = (TextView) findViewById (R.id.tvDetPlace);
		tvDetDate = (TextView) findViewById (R.id.tvDetDate);
		tvDetWeather = (TextView) findViewById(R.id.tvDetWeather);
		tvDetProcess = (TextView) findViewById (R.id.tvDetProcess);
		tvDetCatch = (TextView) findViewById (R.id.tvDetCatch);
		
		 Intent intent = getIntent();
		 String myPlace = intent.getStringExtra("keyPlace");
		 String myDate = intent.getStringExtra("keyDate");
		 String myWeather = intent.getStringExtra("keyWeather");
		 String myProcess = intent.getStringExtra("keyProcess");
		 String myCatch = intent.getStringExtra("keyCatch");
		
		 tvDetPlace.setText("Место рыбалки: " + myPlace);
		 tvDetDate.setText("Дата :" + myDate);
		 tvDetWeather.setText("Погода: " + myWeather);
		 tvDetProcess.setText("Способ рыбалки: " + myProcess);
		 tvDetCatch.setText("Улов: " + myCatch );
	}

}