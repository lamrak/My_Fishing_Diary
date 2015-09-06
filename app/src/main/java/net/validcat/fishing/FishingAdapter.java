package net.validcat.fishing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.validcat.fishing.ui.RoundedImageView;

import java.util.List;

public class FishingAdapter extends RecyclerView.Adapter<FishingAdapter.ViewHolder> {
	private static final String LOG_TAG = FishingAdapter.class.getSimpleName();
	private List<FishingItem> items;
	private Context context;

	public FishingAdapter(Context context, List<FishingItem> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public FishingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		// create a new view and set the view's size, margins, paddings and layout parameters
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_adapter, parent, false));
	}

	@Override
	public void onBindViewHolder(FishingAdapter.ViewHolder viewHolder, int i) {
		FishingItem item = items.get(i);
        viewHolder.view.setOnClickListener(viewHolder);
		viewHolder.place.setText(item.getPlace());
		viewHolder.date.setText(item.getDate());
		viewHolder.discription.setText(item.getDescription());
		
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	// Provide a reference to the views for each data item. Complex data items may need more than
	// one view per item, and you provide access to all the views for a data item in a view holder
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
		public TextView place;
		public TextView date;
		public TextView discription;
		public RoundedImageView foto_preview;
		public RoundedImageView weather_preview;

		public ViewHolder(View view) {
			super(view);
            this.view = view;
			place = (TextView) view.findViewById(R.id.tv_adapter_place);
			date = (TextView) view.findViewById(R.id.tv_adapter_date);
			discription = (TextView) view.findViewById(R.id.tv_adapter_description);
			foto_preview = (RoundedImageView) view.findViewById(R.id.foto_preview);
			weather_preview = (RoundedImageView) view.findViewById(R.id.weather_preview);
		}

		@Override
		public void onClick(View v) {
			int itemPosition = getPosition();
			long id = items.get(itemPosition).getId();
			Log.d(LOG_TAG, " --- ID --- " + id);

			Intent intent = new Intent(context, DetailedFishing.class);
			intent.putExtra("id", id);
			context.startActivity(intent);
		}
	}

}
