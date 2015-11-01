package net.validcat.fishing.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.FishingItem;
import net.validcat.fishing.ListActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.db.DB;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {
    public static final String LOG_TAG = DetailFragment.class.getSimpleName();
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_weather)
    TextView tvWeather;
    @Bind(R.id.tv_description)
    TextView tvDescription;
    @Bind(R.id.tv_catch)
    TextView tvCatch;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    private DB db;
    Bitmap no_photo;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailFragmentView = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, detailFragmentView);

        Bundle arguments = getArguments();
        if (arguments == null) {
            Intent intent = getActivity().getIntent();
            long id = intent.getLongExtra("id", -1);
            db = new DB(getActivity());
            updateUiByItemId(id);

        } else {
            long landId = arguments.getLong(ListActivity.KEY_CLICKED_FRAGMENT);
            Log.d(LOG_TAG, "landId = " + landId);
            db = new DB(getActivity());
            updateUiByItemId(landId);
        }

        return detailFragmentView;
    }

    public void updateUiByItemId(long id) {
        db.open();
        FishingItem item = db.getFishingItemById(id);
        db.close();
        tvPlace.setText(getString(R.string.fishing_place, item.getPlace()));
        tvPlace.setContentDescription(getString(R.string.fishing_place, item.getPlace()));
        tvDate.setText(getString(R.string.fishing_date, item.getDate()));
        tvDate.setContentDescription(getString(R.string.fishing_date, item.getDate()));
        tvWeather.setText(item.getWeather());
//        tvWeather.setText(getString(R.string.fishing_weather, item.getWeather()));
//        tvWeather.setContentDescription(getString(R.string.fishing_weather, item.getWeather()));
        tvDescription.setText(getString(R.string.fishing_description, item.getDescription()));
        tvDescription.setContentDescription(getString(R.string.fishing_description, item.getDescription()));
        tvCatch.setText(getString(R.string.fishing_price, item.getPrice()));
        tvCatch.setContentDescription(getString(R.string.fishing_price, item.getPrice()));
        byte[] photo = item.getPhoto();
        if (photo != null) {
            Log.d(LOG_TAG, "photo !=null " + photo);
            ivPhoto.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
        } else {
            Log.d(LOG_TAG, "photo == null");
            no_photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_photo);
            ivPhoto.setImageBitmap(no_photo);
        }
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getGroupId();
//        return super.onOptionsItemSelected(item);
//    }
}