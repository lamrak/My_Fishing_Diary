package net.validcat.fishing;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	MyListAdapter adapter;
	private static final int ITEM_REQUEST = 0;
	public static final String LOG_TAG = "myLogs";
	DB db;
	Cursor cursor;
	String myWeather;
	

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// open a connection to the database
		db = new DB(this);
		db.open();

	

		adapter = new MyListAdapter(getApplicationContext());
		getListView().setFooterDividersEnabled(true);
		TextView footerView = (TextView) getLayoutInflater().inflate(
				R.layout.footer_view, null);
		getListView().addFooterView(footerView);

		if (null == footerView) {
			return;
		}

		footerView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent startNewActivity = new Intent(MainActivity.this,
						AddNewFishing.class);
				startActivityForResult(startNewActivity, ITEM_REQUEST);

			}

		});

		setListAdapter(adapter);
		// TODO enters
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(MainActivity.this, DetailedFishing.class);
		// intent.putExtra("keyWeather", myWeather);
		startActivity(intent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST) {
			Containe toDo = new Containe(data);
			adapter.add(toDo);
		}
	}

}
