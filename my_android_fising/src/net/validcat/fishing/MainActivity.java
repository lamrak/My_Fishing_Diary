package net.validcat.fishing;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
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
	int idIndex, placeIndex, dateIndex, weatherIndex, processIndex, catchIndex;
	String dataPlace, dataDate, dataWeather, dataProcess, dataCatch;
	long ID;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		// open a connection to the database
		db = new DB(this);
		db.open();
		// read data from the database
		cursor = db.getAllData();
//		id = ID;
		if (cursor.moveToPosition((int) id)) {

			idIndex = cursor.getColumnIndex(DB.COLUMN_ID);
			placeIndex = cursor.getColumnIndex (DB.COLUMN_PLACE);
			dateIndex = cursor.getColumnIndex(DB.COLUMN_DATE);
			weatherIndex = cursor.getColumnIndex(DB.COLUMN_WEATHER);
			processIndex = cursor.getColumnIndex(DB.COLUMN_PROCESS);
			catchIndex = cursor.getColumnIndex(DB.COLUMN_CATCH);

			// do {
			// Log.d(LOG_TAG, " --- ID --- " + cursor.getInt(idIndex)
			// + "--- WEATHER ---" + cursor.getString(weatherIndex));
			// }while (cursor.moveToNext());
			dataPlace = cursor.getString(placeIndex);
			dataDate = cursor.getString(dateIndex);
			dataWeather = cursor.getString(weatherIndex);
			dataProcess = cursor.getString(processIndex);
			dataCatch = cursor.getString(catchIndex);

		} else
			Log.d(LOG_TAG, " --- 0 rows --- ");
		cursor.close();
		db.close();

		Intent intent = new Intent(MainActivity.this, DetailedFishing.class);
		intent.putExtra("keyPlace", dataPlace);
		intent.putExtra("keyDate", dataDate);
		intent.putExtra("keyWeather", dataWeather);
		intent.putExtra("keyProcess", dataProcess);
		intent.putExtra("keyCatch", dataCatch);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST) {
			ID = data.getLongExtra("id", ID);
			Log.d(LOG_TAG, " --- FishingID --- " + ID);

			Container toDo = new Container(data);
			adapter.add(toDo);
		}
	}

}
