package net.validcat.fishing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.models.Fishing;
import net.validcat.fishing.tools.DateUtils;
import net.validcat.fishing.ui.RoundedImageView;

public class FishingViewHolder extends RecyclerView.ViewHolder {
    public TextView place;
    public TextView description;
    public TextView date;
    public ImageView starView;
    public TextView numStarsView;
    public RoundedImageView photoPreview;
    public RoundedImageView weatherPreview;

    public FishingViewHolder(View itemView) {
        super(itemView);

        place = (TextView) itemView.findViewById(R.id.tv_adapter_place);
        date = (TextView) itemView.findViewById(R.id.tv_adapter_date);
        description = (TextView) itemView.findViewById(R.id.tv_adapter_description);
        photoPreview = (RoundedImageView) itemView.findViewById(R.id.photo_preview);
        weatherPreview = (RoundedImageView) itemView.findViewById(R.id.weather_preview);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);

    }

    public void bindToPost(Context context, Fishing fishingItem, View.OnClickListener starClickListener) {
        place.setText(fishingItem.place);
        description.setText(fishingItem.details);
        date.setText(DateUtils.getFullFriendlyDayString(context, fishingItem.date));
        numStarsView.setText(String.valueOf(fishingItem.starCount));

        starView.setOnClickListener(starClickListener);
    }
}
