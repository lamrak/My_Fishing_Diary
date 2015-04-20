package net.validcat.fishing;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	MyListAdapter adapter;
	private static final int ITEM_REQUEST = 0;

	@SuppressLint("InflateParams") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ñîçäàëè àäàïòåð //TODO russian comment
		adapter = new MyListAdapter(getApplicationContext());
		// óñòíàâëèâàåì ðàçäåëèòåëü ìåæäó ñïèñêîì è Ôóòåðîì
		getListView().setFooterDividersEnabled(true);
		//ñîçäàäèì Ôóòåð (èç Layout ôàéëà ñäåëàåì View ýëåìåíò)
		TextView footerView = (TextView)getLayoutInflater().inflate(R.layout.footer_view, null);
		// äîáàâèì ôóòåð â ListView
		getListView().addFooterView(footerView);
		
		if (null == footerView) {
			return;
		}
		
		//ðåàëèçóåì íàæàòèå íà ôóòåð
		footerView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){
				Intent startNewActivity = new Intent (MainActivity.this, FishingList.class);
				startActivityForResult (startNewActivity,ITEM_REQUEST);
			}
			
		});
		// ïðèñîåäèíèì àäàïòåð ê ListView
		setListAdapter (adapter);
		
	//TODO enters
	}
	 @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
		 startActivity(new Intent(MainActivity.this, DetailsFishing.class));
	 }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ïðîâåðÿåì ðåçóëüòàò, äîáîâëÿåì äàííûå ïîëüçîâàòåëÿ â àäàïòåð
		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST ){
			Conteyner toDo = new Conteyner(data); //TODO Conteyner
			adapter.add(toDo);
		}
	}
	
}

