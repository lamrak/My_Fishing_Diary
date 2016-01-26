package net.validcat.fishing.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.AddNewFishingActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.camera.CameraManager;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.models.FishingItem;
import net.validcat.fishing.tools.DateUtils;
import net.validcat.fishing.tools.PrefUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 1;
    @Bind(R.id.tv_place) TextView tvPlace;
    @Bind(R.id.tv_date) TextView tvDate;
    @Bind(R.id.tv_weather) TextView tvWeather;
    @Bind(R.id.tv_description) TextView tvDescription;
    @Bind(R.id.iv_photo) ImageView ivPhoto;
    @Bind(R.id.iv_toolbar_weather_icon) ImageView weatherIcon;
    @Bind(R.id.toolbar) Toolbar toolbar;
    private Uri uri;
    private FishingItem item;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailFragmentView = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, detailFragmentView);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        assert ((AppCompatActivity) getActivity()).getSupportActionBar() != null;
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle arguments = getArguments();
        long id = (arguments != null) ?
                arguments.getLong(Constants.DETAIL_KEY, -1) :
                getActivity().getIntent().getLongExtra(Constants.DETAIL_KEY, -1);
        if (id != -1)
            uri = FishingContract.FishingEntry.buildFishingUri(id);

        return detailFragmentView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null == uri)
            return null;
        return new CursorLoader(getActivity(),
                uri, FishingContract.FishingEntry.COLUMNS, null, null, null);
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(getActivity(),
                        AddNewFishingActivity.class).putExtra(Constants.DETAIL_KEY, uri.toString()));
                return true;
            case R.id.delete:
                getActivity().getContentResolver().delete(uri, null, null);
                getActivity().finish();
                return true;
            default:
                return false;
        }
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            item = FishingItem.createFishingItemFromCursor(getActivity(), data);
            tvPlace.setText(item.getPlace());
            tvPlace.setContentDescription(item.getPlace());
            tvDate.setText(DateUtils.getFullFriendlyDayString(getActivity(), item.getDate()));
            tvDate.setContentDescription(DateUtils.getFullFriendlyDayString(getActivity(), item.getDate()));
            //weather box
            tvWeather.setText(item.getWeather());
            tvWeather.setContentDescription(getString(R.string.fishing_weather, item.getWeather()));
            weatherIcon.setImageResource(PrefUtils.formatWeatherSeletedToIconsCode(item.getWeatherIcon()));
            //content
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(item.getDescription())) sb.append(item.getDescription());
            if (!TextUtils.isEmpty(item.getBait())) sb.append(item.getBait());
            if (!TextUtils.isEmpty(item.getFishFeed())) sb.append(item.getFishFeed());
            if (!TextUtils.isEmpty(item.getCatches())) sb.append(item.getCatches());
            if (!TextUtils.isEmpty(item.getPrice())) sb.append(item.getPrice());

            String descr = sb.toString();
            if (!TextUtils.isEmpty(descr)) {
                tvDescription.setText(getString(R.string.fishing_description, descr));
                tvDescription.setContentDescription(getString(R.string.fishing_description, descr));
            } else {
                tvDescription.setText(getString(R.string.fishing_no_description));
                tvDescription.setContentDescription(getString(R.string.fishing_no_description));
            }

            if (item.getPhotoList() != null && item.getPhotoList().size() > 0) {
                CameraManager.setPic(item.getPhotoList().get(0), ivPhoto);
            } else {
                Log.d(LOG_TAG, "photo == null");
                ivPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_photo));
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @OnClick(R.id.share_fab)
    public void share(View view) {
        startActivity(item.getPhotoList() == null || item.getPhotoList().size() == 0 ?
                ShareCompat.IntentBuilder.from(getActivity())
                        .setText(getString(R.string.fishing_share_message, item.getPlace(),
                                DateUtils.getFullFriendlyDayString(getActivity(),
                                        item.getDate()), item.getDescription()))
                        .setSubject(getString(R.string.app_name))
                        .setType("text/plain")
                        .getIntent() :
                ShareCompat.IntentBuilder.from(getActivity())
                .setText(getString(R.string.fishing_share_message, item.getPlace(),
                        DateUtils.getFullFriendlyDayString(getActivity(),
                                item.getDate()), item.getDescription()))
                .setSubject(getString(R.string.app_name))
                .setStream(Uri.fromFile(new File(item.getPhotoList().get(0))))
                .setType("image/jpeg")
                .getIntent());
    }

}
