package net.validcat.fishing;

import java.util.ArrayList;
import java.util.List;

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
	int ID;
	List<FishingItem> dbList = new ArrayList<FishingItem>();
	FishingItem dbItem;
	FishingItem item;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new DB(this);
		db.open();
		cursor = db.getAllData();

		if (cursor.moveToFirst()) {
			int idBdKey = cursor.getColumnIndex(DB.COLUMN_ID);
			int dbPlaceKey = cursor.getColumnIndex(DB.COLUMN_PLACE);
			int dbDateKey = cursor.getColumnIndex(DB.COLUMN_DATE);

			do {
				int idBd = cursor.getInt(idBdKey);
				String dbPlace = cursor.getString(dbPlaceKey);
				String dbDate = cursor.getString(dbDateKey);

				dbItem = new FishingItem(idBd, dbPlace, dbDate);
				dbList.add(dbItem);
				// Log.d(LOG_TAG, " --- dbList --- " + dbList);
			} while (cursor.moveToNext());
		} else
			cursor.close();
		db.close();

		adapter = new MyListAdapter(getApplicationContext(), dbList);

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

		item = dbList.get(position);
		Log.d(LOG_TAG, " --- item --- " + item);
		ID = item.getId();
		Log.d(LOG_TAG, " --- ID --- " + ID);

		Intent intent = new Intent(MainActivity.this, DetailedFishing.class);
		intent.putExtra("id", ID);
		startActivity(intent);

		// Intent intent = new Intent(MainActivity.this, DetailedFishing.class);
		// intent.putExtra("keyPlace", dataPlace);
		// intent.putExtra("keyDate", dataDate);
		// intent.putExtra("keyWeather", dataWeather);
		// intent.putExtra("keyProcess", dataProcess);
		// intent.putExtra("keyCatch", dataCatch);
		// startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST) {
			FishingItem toDo = new FishingItem(data);

			adapter.add(toDo);

		}
	}

}
