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
	
	private final List<Container> mItems = new ArrayList<Container>();
	private final Context mContext;

	public MyListAdapter(Context context) {
		mContext = context;
	}
// amount elements
	@Override
	public int getCount() {
		return mItems.size();
	}
// position elements
	@Override
	public Object getItem(int pos) {
		return mItems.get(pos);
	}
// id on position
	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	// We receive and display data
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// create users Containe)
		// get data on position (id)
		final Container conteyner =  (Container) getItem (position);
		
		// create View Containe on main.xml
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout itemLayout = (FrameLayout) mInflater.inflate(R.layout.main,  parent, false);
		//find elements
		final TextView place = (TextView)itemLayout.findViewById(R.id.place);
		final TextView date = (TextView)itemLayout.findViewById(R.id.date);
		place.setText(conteyner.getPlace());
		date.setText(conteyner.getDate());
		
		return itemLayout;
	}

	public void add(Container item) {
		// add data in array 
		mItems.add(item);
		// According to neighbors, valid or invalid data
		notifyDataSetChanged();
	}
	
	
}
