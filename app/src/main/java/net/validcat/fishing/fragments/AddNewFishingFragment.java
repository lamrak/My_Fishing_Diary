package net.validcat.fishing.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import net.validcat.fishing.AddNewFishingActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.camera.CameraManager;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.models.FishingItem;
import net.validcat.fishing.tools.BitmapUtils;
import net.validcat.fishing.tools.DateUtils;

import java.util.Calendar;
import java.util.Locale;

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
    private boolean updateData = false;

    private long date = 0;

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

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        AddNewFishingActivity.PERMISSIONS_REQUEST_WRITE_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
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

        date = Calendar.getInstance().getTimeInMillis();
        tvDate.setText(DateUtils.getFullFriendlyDayString(getActivity(), date));

        tvWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                WeatherDialogFragment weatherDialog = new WeatherDialogFragment();
                weatherDialog.show(fm,Constants.DIALOG_KEY);
            }
        });

        return addNewFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.add_new_fishing_action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_add_new_fishing:
                if (TextUtils.isEmpty(etPlace.getText().toString())) {
                    etPlace.setError("Add your fishing place");
                    break;
                }else {
                    ContentValues cv = new ContentValues();
                    if (this.item == null) {
                        this.item = new FishingItem();
                    } else {
                        cv.put(FishingContract.FishingEntry._ID, item.getId());
                    }
                    cv.put(FishingContract.FishingEntry.COLUMN_PLACE, etPlace.getText().toString());
                    cv.put(FishingContract.FishingEntry.COLUMN_DATE, date);
                    cv.put(FishingContract.FishingEntry.COLUMN_WEATHER, tvWeather.getText().toString());
                    cv.put(FishingContract.FishingEntry.COLUMN_DESCRIPTION, etDetails.getText().toString());
                    cv.put(FishingContract.FishingEntry.COLUMN_PRICE, etPrice.getText().toString());

                    if (userPhoto) {
                        Bitmap photo = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();
                        item.setBitmap(photo);
                        cv.put(FishingContract.FishingEntry.COLUMN_IMAGE,
                                BitmapUtils.convertBitmapToBiteArray(((BitmapDrawable) ivPhoto.getDrawable()).getBitmap()));
                    }
                    if (updateData) {
                        getActivity().getContentResolver().update(FishingContract.FishingEntry.CONTENT_URI, cv, null, null);
                    } else {
                        getActivity().getContentResolver().insert(FishingContract.FishingEntry.CONTENT_URI, cv);
                    }

                    getActivity().finish();
                }
                break;

            case R.id.action_camera:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {
                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                AddNewFishingActivity.PERMISSIONS_REQUEST_CAMERA);
                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else runCamera();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public void runCamera() {
        cm = new CameraManager();
        cm.startCameraForResult(getActivity());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            userPhoto = true;
            cm.setPhotoToImageView(getActivity(), requestCode, ivPhoto);
//            Bitmap b = cm.getCameraPhoto(getActivity(), requestCode);
//            if (b != null) {
//                userPhoto = true;
////                ivPhoto.setImageBitmap(CameraManager.scaleDownBitmap(rotate, Constants.HEIGHT_BITMAP, getActivity()));
//                  b = CameraManager.scaleDownBitmap(b, Constants.HEIGHT_BITMAP, getActivity());
//                  ivPhoto.setImageBitmap(b);
//            }
        } else {
            Log.d(LOG_TAG, "onActivityResult returns result not OK");
        }
    }

    public void updateUiByItemId() {
        Cursor cursor = getActivity().getContentResolver().query(uri,
                FishingItem.COLUMNS, null, null, null);
        if(cursor != null){
            if (cursor.moveToFirst()) {
                etPlace.setText(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PLACE)));
            }
        }else{
            if (cursor !=null)
                cursor.close();
        }
        updateData = true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.set(year, monthOfYear, dayOfMonth);
        date =  c.getTimeInMillis(); //(year, monthOfYear, dayOfMonth).getTime();
        Log.d("TIME", "time=" + date);
        tvDate.setText(DateUtils.getFormattedMonthDay(getActivity(), date));
    }


}
