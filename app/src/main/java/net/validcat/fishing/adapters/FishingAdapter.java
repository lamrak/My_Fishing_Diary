package net.validcat.fishing.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.fragments.ListFragment;
import net.validcat.fishing.models.FishingItem;
import net.validcat.fishing.tools.DateUtils;
import net.validcat.fishing.tools.PrefUtils;
import net.validcat.fishing.ui.RoundedImageView;

import java.util.List;

public class FishingAdapter extends RecyclerView.Adapter<FishingAdapter.ViewHolder> { //CursorRecyclerViewAdapter
	private Context context;
	private ListFragment.IClickListener listener;
	private List<FishingItem> items;

	public FishingAdapter(Context context, List<FishingItem> items) {
        this.context = context;
		this.items = items;
	}

	@Override
	public FishingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fishing_list_normal, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
        FishingItem item = items.get(position);
        holder.id = item.getId();
        holder.view.setOnClickListener(holder);
        holder.place.setText(item.getPlace());
        holder.date.setText(DateUtils.getFullFriendlyDayString(context, item.getDate()));
        holder.description.setText(item.getDescription());

        if (!TextUtils.isEmpty(item.getThumb())) {
        Bitmap photo = BitmapFactory.decodeFile(item.getThumb());
        if (photo != null) holder.photoPreview.setImageBitmap(photo);
        else holder.photoPreview.setImageResource(R.drawable.ic_no_photo);
        } else {
        holder.photoPreview.setImageResource(R.drawable.ic_no_photo);
        }

        holder.weatherPreview.setImageResource(PrefUtils.formatWeatherSeletedToIconsCode(item.getWeatherIcon()));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

    public void setIClickListener(ListFragment.IClickListener listener) {
		this.listener = listener;
	}

	// Provide a reference to the views for each data item. Complex data items may need more than
	// one view per item, and you provide access to all the views for a data item in a view holder
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
		public TextView place;
		public TextView date;
		public TextView description;
		public RoundedImageView photoPreview;
		public RoundedImageView weatherPreview;

        long id;

		public ViewHolder(View view) {
			super(view);
            this.view = view;
			place = (TextView) view.findViewById(R.id.tv_adapter_place);
			date = (TextView) view.findViewById(R.id.tv_adapter_date);
			description = (TextView) view.findViewById(R.id.tv_adapter_description);
			photoPreview = (RoundedImageView) view.findViewById(R.id.photo_preview);
			weatherPreview = (RoundedImageView) view.findViewById(R.id.weather_preview);
		}

		@Override
		public void onClick(View v) {
			listener.onItemClicked(id, description, date, weatherPreview);
		}
	}

}
