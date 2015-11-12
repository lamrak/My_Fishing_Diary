package net.validcat.fishing.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import net.validcat.fishing.FishingItem;
import net.validcat.fishing.ListActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.db.Constants;
import net.validcat.fishing.db.DB;

import java.io.ByteArrayOutputStream;

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
    Bitmap myPhoto;
    long id;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailFragmentView = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, detailFragmentView);

        Bundle arguments = getArguments();
        if (arguments == null) {
            Intent intent = getActivity().getIntent();
            id = intent.getLongExtra("id", -1);
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
        Bitmap photo = item.getBitmap();
        if (photo != null) {
            Log.d(LOG_TAG, "photo !=null " + photo);
            ivPhoto.setImageBitmap(photo);
        } else {
            Log.d(LOG_TAG, "photo == null");
            ivPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_photo));
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                 + tvCatch.getText() + "\n";
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.share)
            share();
        else if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(getActivity(), AddNewFishingActivity.class);
            intent.putExtra(Constants.DETAIL_KEY, id);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}