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
	public static final String LOG_TAG = "myLogs";
	private static final int ITEM_REQUEST = 0;
	
	MyListAdapter adapter;

	DB db;
	Cursor cursor;

	List<FishingItem> itemsList = new ArrayList<FishingItem>();

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DB(this);
		db.open();
		cursor = db.getAllData(); //TODO move this in DB
		if (cursor.moveToFirst()) {
			itemsList = db.getData(itemsList, cursor);
		} else
			cursor.close();
		db.close();

		adapter = new MyListAdapter(getApplicationContext(), itemsList);

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
		FishingItem item = itemsList.get(position);
		id = item.getId();
		Log.d(LOG_TAG, " --- ID --- " + id);

		Intent intent = new Intent(MainActivity.this, DetailedFishing.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST) {
			FishingItem toDo = new FishingItem(data);
			adapter.add(toDo);

		}
	}

}
