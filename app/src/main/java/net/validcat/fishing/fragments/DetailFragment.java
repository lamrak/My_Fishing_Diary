package net.validcat.fishing.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.validcat.fishing.FishingItem;
import net.validcat.fishing.R;
import net.validcat.fishing.db.DB;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    public DetailFragment() {}

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
    private byte[] photo;
    Bitmap myPhoto;

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

        Intent intent = getActivity().getIntent();
        long id = intent.getLongExtra("id", -1);
        db = new DB(getActivity());
        updateUiByItemId(id);

        Bundle arguments = getArguments();
        if (arguments != null) {
            long landId = arguments.getLong("fragment");
            db = new DB(getActivity());
            updateUiByItemId(landId);
        }
        return detailFragmentView;
    }

    public void updateUiByItemId(long id) {
        db.open();
        FishingItem item = db.getFishingItemById(id);
        db.close();
        tvPlace.setText("Place: " + item.getPlace());
        tvDate.setText("Date: " + item.getDate());
        tvWeather.setText("Weather: " + item.getWeather());
        tvDescription.setText("Description: " + item.getDescription());
        tvCatch.setText("Price: " + item.getPrice());
        photo = item.getPhoto();
        myPhoto = BitmapFactory.decodeByteArray(photo,0,photo.length);
        ivPhoto.setImageBitmap(myPhoto);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_detail_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share)
            share();
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        // create Intent to share urlString
        String massage = (String)null;
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject);
        massage = tvPlace.getText() + "\n"
                 + tvDate.getText() + "\n"
                 + tvWeather.getText() + "\n"
                 + tvDescription.getText() + "\n"
                 + tvCatch.getText() + "\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, massage);
        Bitmap icon = myPhoto;
        shareIntent.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));

        // display apps that can share text
        startActivity(Intent.createChooser(shareIntent,getString(R.string.share_search)));
    }
}