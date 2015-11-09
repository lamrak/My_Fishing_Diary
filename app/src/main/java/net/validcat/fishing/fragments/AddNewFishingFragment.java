package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import net.validcat.fishing.db.Constants;
import net.validcat.fishing.db.DB;
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
    private DB db;
    private long id;
    private boolean userPhoto = false;

    public AddNewFishingFragment() {
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View addNewFragmentView = inflater.inflate(R.layout.add_new_fishing_fragment, container, false);
        ButterKnife.bind(this, addNewFragmentView);

        Intent intent = getActivity().getIntent();
        id = intent.getLongExtra(Constants.DETAIL_KEY, -1);
        db = new DB(getActivity());

        if (id != -1)
            updateUiByItemId();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                FishingItem items = new FishingItem();
                if (userPhoto) {
                    byte[] photo = CameraManager.getByteArrayFromBitmap(((BitmapDrawable) ivPhoto.getDrawable()).getBitmap());
                    if (photo != null) {
                        items.setPhoto(photo);
                    }
                }
                items.setId(id);
                items.setPlace(etPlace.getText().toString());
                items.setDate(tvDate.getText().toString());
                items.setWeather(tvWeather.getText().toString());
                items.setDescription(etDetails.getText().toString());
                items.setPrice(etPrice.getText().toString());

                // open a connection to the database
                db = new DB(getActivity());
                db.open();
                long id = db.saveFishingItem(items);
                items.setId(id);
                db.close();

                Intent data = new Intent();
                FishingItem.packageIntentFromItem(data, items);
                //send container
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
                break;

            case R.id.action_camera:
                cm = new CameraManager();
                cm.startCameraForResult(getActivity());

                break;
        }

        return super.onOptionsItemSelected(item);
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
        db.open();
        FishingItem item = db.getFishingItemById(id);
        db.close();
        etPlace.setText( item.getPlace());

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        tvDate.setText(String.format("%d.%d.%d", dayOfMonth, ++monthOfYear, year));
    }

}