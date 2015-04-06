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
		
		// создали адаптер
		adapter = new MyListAdapter(getApplicationContext());
		
		// устнавливаем разделитель между списком и Футером
		getListView().setFooterDividersEnabled(true);
		
		//создадим Футер (из Layout файла сделаем View элемент)

		TextView footerView = (TextView)getLayoutInflater().inflate(R.layout.footer_view, null);
		
		// добавим футер в ListView
		getListView().addFooterView(footerView);
		
		if (null == footerView) {
			return;
		}
		
		//реализуем нажатие на футер
		footerView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){
				Intent startNewActivity = new Intent (MainActivity.this, FishingList.class);
				startActivityForResult (startNewActivity,ITEM_REQUEST);
			}
			
		});
		// присоединим адаптер к ListView
		setListAdapter (adapter);
		
		
		
		
	}
	 @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
		 startActivity(new Intent(MainActivity.this, DetailsFishing.class));
	 }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// проверяем результат, добовляем данные пользователя в адаптер
		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST ){
			Conteyner toDo = new Conteyner(data);
			adapter.add(toDo);
		}
	}
	
}

