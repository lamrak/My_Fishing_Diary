package net.validcat.fishing;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends ListActivity {
	MyListAdapter mAdapter;
	private static final int ITEM_REQUEST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// создали адаптер
		mAdapter = new MyListAdapter(getApplicationContext());
		
		// устнавливаем разделитель между списком и Футером
		getListView().setFooterDividersEnabled(true);
		
		// свяжем футер с xml
		LayoutInflater inflater = (LayoutInflater)getApplicationContext().
		getSystemService (LAYOUT_INFLATER_SERVICE);
		TextView footerView = (TextView)getLayoutInflater().inflate(R.layout.footer_view, null);
		
		// добавим футер в ListView
		getListView().addFooterView(footerView);
		
		if (null == footerView) {
			return;
		}
		
		//реализуем нажатие на футер
		footerView.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v){
				Intent startNewActivity = new Intent (MainActivity.this,FishingList.class);
				startActivityForResult (startNewActivity,ITEM_REQUEST);
			}
			
		});
		// присоединим адаптер к ListView
		setListAdapter (mAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// проверяем результат, добовляем данные пользователя в адаптер
		if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST ){
			ToDoItem toDo = new ToDoItem (data);
			mAdapter.add(toDo);
			
		}
	}
}

