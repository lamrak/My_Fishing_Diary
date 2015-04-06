package net.validcat.fishing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class DetailsFishing extends Activity {

//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		etWeather.setText(savedInstanceState.getString("weather"));
//		etProcess.setText(savedInstanceState.getString("process"));
//		etCatch.setText(savedInstanceState.getString("catch"));
//	}

	EditText etWeather, etProcess, etCatch;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_fishing);

		etWeather = (EditText) findViewById(R.id.etWeather);
		etProcess = (EditText) findViewById(R.id.etProcess);
		etCatch = (EditText) findViewById(R.id.etCatch);
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("weather", etWeather.getText().toString());
		outState.putString("process", etProcess.getText().toString());
		outState.putString("catch", etCatch.getText().toString());
		 super.onSaveInstanceState(outState);
	}
}
