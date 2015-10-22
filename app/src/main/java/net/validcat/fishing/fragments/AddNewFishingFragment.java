package net.validcat.fishing.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.FishingItem;
import net.validcat.fishing.R;
import net.validcat.fishing.db.DB;
import net.validcat.fishing.tools.CameraManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Denis on 11.09.2015.
 */
public class AddNewFishingFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = AddNewFishingFragment.class.getSimpleName();
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.et_place)
    EditText etPlace;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_weather)
    TextView tvWeather;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.et_details)
    EditText etDetails;

    private CameraManager cm;

    public AddNewFishingFragment() {
        setHasOptionsMenu(true);
    }

    private DB db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View addNewFragmentView = inflater.inflate(R.layout.add_new_fishing_fragment, container, false);
        ButterKnife.bind(this, addNewFragmentView);



       // fab_add_fishing_list.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date(System.currentTimeMillis()));

        tvDate.setText(date);
//
//        Calendar c = Calendar.getInstance();
//        day = c.get(Calendar.DAY_OF_MONTH);
//        month = c.get(Calendar.MONTH);
//        year = c.get(Calendar.YEAR);

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
                String myPlace = etPlace.getText().toString();
                String myDate = tvDate.getText().toString();
                String myWeather = tvWeather.getText().toString();
                String myDescription = etDetails.getText().toString();
                String myPrice = etPrice.getText().toString();

                Bitmap b = ((BitmapDrawable) ivPhoto.getDrawable()).getBitmap();
                byte[] photo = CameraManager.getByteArrayfromBitmap(b);

                FishingItem items = new FishingItem();
                items.setPlace(myPlace);
                items.setDate(myDate);
                items.setWeather(myWeather);
                items.setDescription(myDescription);
                items.setPrice(myPrice);

                if (photo != null)
                    items.setPhoto(photo);
                else Log.d(LOG_TAG, "photo == null");

                // open a connection to the database
                db = new DB(getActivity());
                db.open();
                long id = db.saveFishingItem(items);
                db.close();

                Intent data = new Intent();
                FishingItem.packageIntent(data, myPlace, myDate, id, myDescription, b);
                // send container
//                setResult(RESULT_OK, data); //TODO
//                finish();
                break;

            case R.id.action_camera:
                cm = new CameraManager();
                cm.startCameraForResult(getActivity());
                
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.fab_add_fishing_list:
//                // save in data base
//                String myPlace = etPlace.getText().toString();
//                String myDate = tvDate.getText().toString();
//                String myWeather = tvWeather.getText().toString();
//                String myDescription = etDetails.getText().toString();
//                String myPrice = etPrice.getText().toString();
//
//                FishingItem item = new FishingItem();
//                item.setPlace(myPlace);
//                item.setDate(myDate);
//                item.setWeather(myWeather);
//                item.setDescription(myDescription);
//                item.setPrice(myPrice);
//
//                // open a connection to the database
//                db = new DB(getActivity());
//                db.open();
//                long id = db.saveFishingItem(item);
//                db.close();
//
//                Intent data = new Intent();
//                FishingItem.packageIntent(data, myPlace, myDate, id, myDescription);
//                // send container
//                getActivity().setResult(Activity.RESULT_OK, data);
//                getActivity().finish();
//                break;
            case R.id.tv_date:
                DialogFragment picker = new DatePickerFragment(tvDate);
                picker.show(getFragmentManager(), "datePicker");
                break;
//            case R.id.iv_photo:
//                cm = new CameraManager();
//                cm.startCameraForResult(getActivity());
//                break;
        }
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Bitmap b = cm.extractPhotoBitmapFromResult(requestCode, resultCode, data);
        Bitmap b = cm.getCameraPhoto(getActivity());
        b = cm.scaleDownBitmap(b, 200, getActivity()); //TODO what is 200????
        if (b != null) {
            ivPhoto.setImageBitmap(b);
            Log.d(LOG_TAG, "Intent data onActivityResult == " +b);
        } else {
            Log.d(LOG_TAG, "Intent data onActivityResult == null");
        }
    }


}