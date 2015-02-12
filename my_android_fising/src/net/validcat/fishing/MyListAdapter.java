package net.validcat.fishing;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {
	
	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	private final Context mContext;

	public MyListAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int pos) {
		return mItems.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	// получаем и отображаем данные
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// создаем пользовательский Контейнер (ToDoItem)
		// получаем данные по позиции (id)
		final ToDoItem toDoItem =  (ToDoItem) getItem (position);
		
		// cвязываем View нашего контейнера с main.xml
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout itemLayout = (FrameLayout) mInflater.inflate(R.layout.main,  parent, false);
		//находим элемент
		final TextView place = (TextView)itemLayout.findViewById(R.id.place);
		final TextView date = (TextView)itemLayout.findViewById(R.id.date);
		place.setText(toDoItem.getPlace());
		date.setText(toDoItem.getDate());
		
		return itemLayout;
	}

	public void add(ToDoItem item) {
		// добавили данные в массив (список)
		mItems.add(item);
		// сообщает соседям действительны или недействительны данные
		notifyDataSetChanged();
	}

}
