package net.validcat.fishing.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 1;
    @Bind(R.id.tv_place) TextView tvPlace;
    @Bind(R.id.tv_date) TextView tvDate;
    @Bind(R.id.tv_weather) TextView tvWeather;
    @Bind(R.id.tv_description) TextView tvDescription;
    @Bind(R.id.tv_price) TextView tvPrice;
    @Bind(R.id.iv_photo) ImageView ivPhoto;

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
                Intent intent = new Intent(getActivity(), AddNewFishingActivity.class);
                intent.putExtra(Constants.DETAIL_KEY, uri.toString());
                startActivity(intent);
                return true;
            case R.id.share:
                share();
                break;
            case R.id.delete:
                getActivity().getContentResolver().delete(uri, null, null);
                getActivity().finish();
                return true;
            default:
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {
        // create Intent to share urlString
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject);
        String massage = tvPlace.getText() + "\n"
                + tvDate.getText() + "\n"
                + tvWeather.getText() + "\n"
                + tvDescription.getText() + "\n"
                + tvPrice.getText() + "\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, massage);
        shareIntent.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //TODO get link to bitmap from fishing item
//        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
//        try {
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            fo.write(bytes.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));

        // display apps that can share text
        startActivity(Intent.createChooser(shareIntent,getString(R.string.share_search)));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            item = FishingItem.createFishingItemFromCursor(getActivity(), data);
            //TODO add content description for each TextView
            tvPlace.setText(getString(R.string.fishing_place, item.getPlace()));
            tvPlace.setContentDescription(getString(R.string.fishing_place, item.getPlace()));
            tvDate.setText(DateUtils.getFullFriendlyDayString(getActivity(), item.getDate()));
            //tvDate.setText(getString(R.string.fishing_date, item.getDate()));
            tvDate.setContentDescription(getString(R.string.fishing_date, item.getDate()));
            tvWeather.setText(item.getWeather());
//        tvWeather.setText(getString(R.string.fishing_weather, item.getWeather()));
//        tvWeather.setContentDescription(getString(R.string.fishing_weather, item.getWeather()));
            tvDescription.setText(getString(R.string.fishing_description, item.getDescription()));
            tvDescription.setContentDescription(getString(R.string.fishing_description, item.getDescription()));
            tvPrice.setText(getString(R.string.fishing_price, item.getPrice()));
            tvPrice.setContentDescription(getString(R.string.fishing_price, item.getPrice()));
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
}
