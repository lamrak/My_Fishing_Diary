package net.validcat.fishing.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.FishingItem;
import net.validcat.fishing.R;
import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.db.Constants;
import net.validcat.fishing.tools.CameraManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Denis on 11.09.2015.
 */
public class AddNewFishingFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String LOG_TAG = AddNewFishingFragment.class.getSimpleName();
    @Bind(R.id.iv_photo) ImageView ivPhoto;
    @Bind(R.id.et_place) EditText etPlace;
    @Bind(R.id.tv_date) TextView tvDate;
    @Bind(R.id.tv_weather) TextView tvWeather;
    @Bind(R.id.et_price) EditText etPrice;
    @Bind(R.id.et_details) EditText etDetails;

    private CameraManager cm;
    private Uri uri;
    private FishingItem item;
    private boolean userPhoto = false;

    public AddNewFishingFragment() {
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View addNewFragmentView = inflater.inflate(R.layout.add_new_fishing_fragment, container, false);
        ButterKnife.bind(this, addNewFragmentView);

        Intent intent = getActivity().getIntent();
        String strUri = intent.getStringExtra(Constants.DETAIL_KEY);

        if (!TextUtils.isEmpty(strUri)) {
            uri = Uri.parse(strUri);
            updateUiByItemId();
        }

       // fab_add_fishing_list.setOnClickListener(this);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFragment() {

                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {

                        // Use the current date as the default date in the picker
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        // Create a new instance of DatePickerDialog and return it
                        return new DatePickerDialog(getActivity(), AddNewFishingFragment.this, year, month, day);
                    }
                }.show(getFragmentManager(), "datePicker");
            }
        });

        tvDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis())));

        return addNewFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.add_new_fishing_action_bar, menu);
//            finishCreatingMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_add_new_fishing:
                ContentValues cv = new ContentValues();
                if (this.item == null) {
                    this.item = new FishingItem();
                } else {
                    cv.put(FishingContract.FishingEntry._ID, item.getId());
                }
                cv.put(FishingContract.FishingEntry.COLUMN_PLACE, etPlace.getText().toString());
                cv.put(FishingContract.FishingEntry.COLUMN_DATE, tvDate.getText().toString());
                cv.put(FishingContract.FishingEntry.COLUMN_WEATHER, tvWeather.getText().toString());
                cv.put(FishingContract.FishingEntry.COLUMN_DESCRIPTION, etDetails.getText().toString());
                cv.put(FishingContract.FishingEntry.COLUMN_PRICE, etPrice.getText().toString());
//                cv.put(FishingContract.FishingEntry.COLUMN_, );
                if (userPhoto) {
                    cv.put(FishingContract.FishingEntry.COLUMN_IMAGE,
                            CameraManager.getByteArrayFromBitmap(((BitmapDrawable) ivPhoto.getDrawable()).getBitmap()));
                }

                getActivity().getContentResolver().insert(FishingContract.FishingEntry.CONTENT_URI, cv);
                getActivity().finish();
                break;

            case R.id.action_camera:
                cm = new CameraManager();
                cm.startCameraForResult(getActivity());

                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Bitmap b = cm.extractPhotoBitmapFromResult(requestCode, resultCode, data);
        Bitmap b = cm.getCameraPhoto();
        if (b != null) {
            userPhoto = true;
            ivPhoto.setImageBitmap(CameraManager.scaleDownBitmap(b, 200, getActivity())); //TODO what is 200????
        } else {
            Log.d(LOG_TAG, "Intent data onActivityResult == null");
        }
    }

    public void updateUiByItemId() {
        item = (FishingItem) getActivity().getContentResolver().query(uri,
                FishingItem.COLUMNS, null, null, null); //db.getFishingItemById(id);
        etPlace.setText(item.getPlace());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        tvDate.setText(String.format("%d.%d.%d", dayOfMonth, ++monthOfYear, year));
    }

}