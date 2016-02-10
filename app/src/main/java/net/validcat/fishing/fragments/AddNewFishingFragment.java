package net.validcat.fishing.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.validcat.fishing.AddNewFishingActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.SettingsActivity;
import net.validcat.fishing.camera.CameraManager;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.data.FishingContract.FishingEntry;
import net.validcat.fishing.models.FishingItem;
import net.validcat.fishing.tools.DateUtils;
import net.validcat.fishing.tools.PrefUtils;
import net.validcat.fishing.weather.WeatherSyncFetcher;
import org.json.JSONException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AddNewFishingFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, LocationListener {
    public static final String LOG_TAG = AddNewFishingFragment.class.getSimpleName();
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
    @Bind(R.id.iv_weather)
    ImageView ivWeather;
    //    @Bind(R.id.tv_tackle) TextView tvTackle;
//    @Bind(R.id.iv_tackle) ImageView ivTackle;
    @Bind(R.id.et_bait)
    EditText etBait;
    @Bind(R.id.et_fish_feed)
    EditText etFishFeed;
//    @Bind(R.id.et_catch) EditText etCatch;

    //tackle
    @Bind(R.id.tv_tackle_value)
    TextView tvTackleValue;
    @Bind(R.id.ic_rod)
    Button rod;
    @Bind(R.id.ic_spinning)
    Button spinning;
    @Bind(R.id.ic_feeder)
    Button feeder;
    @Bind(R.id.ic_distance_casting)
    Button casting;
    @Bind(R.id.ic_ice_fishing_rod)
    Button iceRod;
    @Bind(R.id.ic_tip_up)
    Button tipUp;
    @Bind(R.id.ic_hand_line)
    Button handLine;
    @Bind(R.id.ic_fly_fishing)
    Button flyFishing;
    private int[] selectedIdx;
    private String[] tackles;

    private CameraManager cm;
    private Uri uri;
    private FishingItem item;
    private boolean updateData = false;
    //weather
    private int weatherIconSelection = 0;
    private int weatherTemp = 0;
    //    private int tackleSelection = 0;
    // private int tackleTextSelection = 0;
    private String photoPath;
    private String photoId;
    private long date = 0;
//    private int editWeather;
//    private boolean checkWeather = false;

    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private Location location;
    private String provider;

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
            updateData = true;
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        }

        // fab_add_fishing_list.setOnClickListener(this);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFragment() {

                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AddNewFishingFragment.this, year, month, day);
                        dialog.getDatePicker().setMaxDate(new Date().getTime());
                        return dialog;
//                        return new DatePickerDialog(getActivity(), AddNewFishingFragment.this, year, month, day);
                    }
                }.show(getFragmentManager(), "datePicker");
            }
        });

        if (date == 0)
            date = Calendar.getInstance().getTimeInMillis();
        tvDate.setText(DateUtils.getFullFriendlyDayString(getActivity(), date));

        View.OnClickListener lin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runWeatherDialog();

            }
        };
        tvWeather.setOnClickListener(lin);
        ivWeather.setOnClickListener(lin);

        initTackleUI();

        if (updateData) {
            updateWeatherData(PrefUtils.getFormattedTemp(getActivity(), weatherTemp),
                    PrefUtils.formatWeatherSeletedToIconsCode(weatherIconSelection));
        } else {
            makeWeatherRequest();
        }

        cm = new CameraManager();

        getCurrentLocation();

        return addNewFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        locationManager.requestLocationUpdates(provider,
                Constants.LOCATION_MIN_TIME, Constants.LOCATION_MIN_DISTANCE, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        locationManager.removeUpdates(this);
    }

    private void initTackleUI() {
        rod.setOnClickListener(this);
        spinning.setOnClickListener(this);
        feeder.setOnClickListener(this);
        casting.setOnClickListener(this);
        iceRod.setOnClickListener(this);
        tipUp.setOnClickListener(this);
        handLine.setOnClickListener(this);
        flyFishing.setOnClickListener(this);

        tackles = getResources().getStringArray(R.array.tackle_array);
        selectedIdx = new int[tackles.length];
    }

    @Override
    public void onClick(View v) {
        v.setSelected(!v.isSelected());
        int ind = -1;
        switch (v.getId()) {
            case R.id.ic_rod:
                tvTackleValue.setText(R.string.rod);
                ind = 0;
                break;
            case R.id.ic_spinning:
                tvTackleValue.setText(R.string.spinning);
                ind = 1;
                break;
            case R.id.ic_feeder:
                tvTackleValue.setText(R.string.feeder);
                ind = 2;
                break;
            case R.id.ic_distance_casting:
                tvTackleValue.setText(R.string.distance_casting);
                ind = 3;
                break;
            case R.id.ic_ice_fishing_rod:
                tvTackleValue.setText(R.string.ice_fishing_rod);
                ind = 4;
                break;
            case R.id.ic_tip_up:
                tvTackleValue.setText(R.string.tip_up);
                ind = 5;
                break;
            case R.id.ic_hand_line:
                tvTackleValue.setText(R.string.hand_line);
                ind = 6;
                break;
            case R.id.ic_fly_fishing:
                tvTackleValue.setText(R.string.fly_fishing);
                ind = 7;
                break;
        }

        if (ind == -1) {
            return;
        }
        selectedIdx[ind] = selectedIdx[ind] == 0 ? 1 : 0;
        updateTextView();
    }

    private void updateTextView() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedIdx.length; i++) {
            if (selectedIdx[i] == 1) {
                sb.append(tackles[i]);
                sb.append(", ");
            }
        }

        if (sb.length() > 0)
            tvTackleValue.setText(sb.substring(0, sb.length() - 2));
    }

    private void makeWeatherRequest() {
        new FetcherTask(new WeatherSyncFetcher.IWeatherListener() {
            @Override
            public void onWeatherResult(int id, double temp, double high, double low) {
                weatherIconSelection = PrefUtils.formatWeatherIdToSelection(id);
                weatherTemp = (int) PrefUtils.formatTemperatureToMetrics(getActivity(), temp);

                updateWeatherData(PrefUtils.formatTemperature(getActivity(), temp),
                        PrefUtils.formatWeatherIdToIconsCode(id));
            }
        }).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.add_new_fishing_action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (!TextUtils.isEmpty(etPlace.getText()) && !updateData) {
                    showConfirmationDialog();
                    return true;
                }
                break;
            case R.id.action_add_new_fishing:
                storeNewFishing();
                break;
            case R.id.action_camera:
                runPhotoDialog();
                break;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void showConfirmationDialog() {
        DialogFragment newFragment = StoreAlertDialog.newInstance(R.string.close_without_save);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void handleCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        Constants.PERMISSIONS_REQUEST_CAMERA);
                return;
            }
        }
        runCamera();
    }

    private void storeNewFishing() {
        if (TextUtils.isEmpty(etPlace.getText().toString())) {
            etPlace.setError(Constants.VALIDATION_ERROR);
        } else {
            ContentValues cv = new ContentValues();
            if (this.item != null)
                cv.put(FishingEntry._ID, item.getId());

            cv.put(FishingEntry.COLUMN_PLACE, etPlace.getText().toString());
            cv.put(FishingEntry.COLUMN_DATE, date);
            cv.put(FishingEntry.COLUMN_WEATHER, tvWeather.getText().toString());
            cv.put(FishingEntry.COLUMN_DESCRIPTION, etDetails.getText().toString());
            cv.put(FishingEntry.COLUMN_PRICE, etPrice.getText().toString());

            if (!TextUtils.isEmpty(photoPath)) {
                String thumbPath = cm.createAndSaveThumb(photoPath, photoId);
                cv.put(FishingEntry.COLUMN_IMAGE, photoPath);
                cv.put(FishingEntry.COLUMN_THUMB, thumbPath);
            }
            cv.put(FishingEntry.COLUMN_WEATHER_ICON, weatherIconSelection);
//            cv.put(FishingEntry.COLUMN_TACKLE, tvTackle.getText().toString());
            cv.put(FishingEntry.COLUMN_BAIT, etBait.getText().toString());
            cv.put(FishingEntry.COLUMN_FISH_FEED, etFishFeed.getText().toString());
//            cv.put(FishingEntry.COLUMN_CATCH, etCatch.getText().toString());

            if (updateData) {
                getActivity().getContentResolver().update(FishingEntry.CONTENT_URI, cv, null, null);
            } else {
                getActivity().getContentResolver().insert(FishingEntry.CONTENT_URI, cv);
            }

            getActivity().finish();
        }
    }

    public void runCamera() {
        photoId = new SimpleDateFormat(Constants.CAMERA_TIME_PATTERN, Locale.getDefault()).format(new Date());
        cm.startCameraForResult(getActivity(), photoId);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case Constants.REQUEST_CODE_PHOTO:
                ivPhoto.setVisibility(View.VISIBLE);
                photoPath = cm.setPhotoToImageView(getActivity(), requestCode, ivPhoto);
                break;
            case Constants.REQUEST_TEMPERATURE:
                weatherIconSelection = data.getIntExtra(Constants.EXTRA_IMAGE_KEY, -1);
                weatherTemp = data.getIntExtra(Constants.EXTRA_TEMPERATURE, 0);
                updateWeatherData(PrefUtils.getFormattedTemp(getActivity(), weatherTemp),
                        PrefUtils.formatWeatherSeletedToIconsCode(weatherIconSelection));
                break;
            case Constants.REQUEST_TAKE_PHOTO:
                handleCamera();
                break;
            case Constants.REQUEST_PICK_PHOTO:
                ivPhoto.setVisibility(View.VISIBLE);
                //http://stackoverflow.com/questions/20260416/retrieve-absolute-path-when-select-image-from-gallery-kitkat-android
                Uri selectedImage = Uri.parse(data.getStringExtra(Constants.IMAGE_URI));
                photoId = CameraManager.getPhotoIdFromUri(getActivity(), selectedImage);
                photoPath = CameraManager.getPath(getActivity(), selectedImage);
                setImage(selectedImage);
                break;
//            case Constants.REQUEST_TACKLE:
//                //PrefUtils.formatTacleSeletedToTextView(tackleSelection),
//                updateTackleData(PrefUtils.formatTacleSeletedToIconsCode(data.getIntExtra(Constants.EXTRA_TACKLE_IMAGE_KEY,-1)));

        }
    }

    private void updateWeatherData(String temp, int weather) {
        tvWeather.setText(temp);
        ivWeather.setImageResource(weather);
        // ivWeather.setImageResource(checkWeather ? editWeather : weather);
    }

//    private void updateTackleData(int iconTackle) {
//        ivTackle.setImageResource(iconTackle);
//    }

    public void updateUiByItemId() {
        Cursor cursor = getActivity().getContentResolver().query(uri,
                FishingEntry.COLUMNS, null, null, null);
        if (cursor == null)
            return;

        if (cursor.moveToFirst()) {
            etPlace.setText(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PLACE)));
            etPrice.setText(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PRICE)));
            etDetails.setText(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_DESCRIPTION)));
            etBait.setText(cursor.getString(cursor.getColumnIndex(FishingEntry.COLUMN_BAIT)));
            etFishFeed.setText(cursor.getString(cursor.getColumnIndex(FishingEntry.COLUMN_FISH_FEED)));
            photoPath = cursor.getString(cursor.getColumnIndex(FishingEntry.COLUMN_IMAGE));
            if (!TextUtils.isEmpty(photoPath)) {
                ivPhoto.setVisibility(View.VISIBLE);
                CameraManager.setPic(photoPath, ivPhoto);
            }
            date = cursor.getLong(cursor.getColumnIndex(FishingEntry.COLUMN_DATE));
            weatherIconSelection = cursor.getInt(cursor.getColumnIndex(FishingEntry.COLUMN_WEATHER_ICON));
            weatherTemp = cursor.getInt(cursor.getColumnIndex(FishingEntry.COLUMN_WEATHER));
            cursor.close();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.set(year, monthOfYear, dayOfMonth);
        date = c.getTimeInMillis(); //(year, monthOfYear, dayOfMonth).getTime();
        Log.d("TIME", "time=" + date);
        tvDate.setText(DateUtils.getFormattedMonthDay(getActivity(), date));
    }

    private void runWeatherDialog() {
        FragmentManager fm = getActivity().getFragmentManager();
        WeatherDialogFragment weatherDialog = new WeatherDialogFragment();
        weatherDialog.setTargetFragment(AddNewFishingFragment.this, Constants.REQUEST_TEMPERATURE);

        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_IMAGE_KEY, weatherIconSelection);
        args.putInt(Constants.EXTRA_TEMPERATURE, weatherTemp);
        weatherDialog.setArguments(args);

        weatherDialog.show(fm, Constants.DIALOG_KEY);
    }

    private void runPhotoDialog() {
        FragmentManager fm = getActivity().getFragmentManager();
        PhotoDialogFragment photoDialog = new PhotoDialogFragment();
        photoDialog.setTargetFragment(AddNewFishingFragment.this, Constants.REQUEST_TAKE_PHOTO);
        photoDialog.show(fm, Constants.PHOTO_DIALOG_KEY);
    }

    private void runTackleDialog() {
        FragmentManager fm = getActivity().getFragmentManager();
        TackleDialogFragment tackleDialog = new TackleDialogFragment();
        tackleDialog.setTargetFragment(AddNewFishingFragment.this, Constants.REQUEST_TACKLE);
        tackleDialog.show(fm, Constants.TACKLE_DIALOG_KEY);
    }

    public void setImage(Uri imageUri) {
        Picasso.with(getActivity()).load(imageUri).into(ivPhoto);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = (location.getLatitude());
        longitude = (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(LOG_TAG, "Enabled new provider " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(LOG_TAG, "Disabled provider " + provider);
    }

    public void getCurrentLocation() {
        handleLocation();

        // Initialize the location fields
        if (location != null) {
            Log.i(LOG_TAG, "Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }

        // write location in preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(Constants.PREFERENCES_LOCATION_LATITUDE,
                Double.doubleToLongBits(location.getLatitude()));
        editor.putLong(Constants.PREFERENCES_LOCATION_LONGITUDE,
                Double.doubleToLongBits(location.getLongitude()));
        editor.apply();

        Log.i(LOG_TAG, String.valueOf(latitude)+ " and " + String.valueOf(longitude));
    }

    private void handleLocation() {
        // Get the location manager
        locationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        location = locationManager.getLastKnownLocation(provider);
    }

    private class FetcherTask extends AsyncTask<Void, Void, String> {
        private WeatherSyncFetcher.IWeatherListener listener;
        private WeatherSyncFetcher syncAdapter;

        public FetcherTask(WeatherSyncFetcher.IWeatherListener listener) {
            this.listener = listener;
            syncAdapter = new WeatherSyncFetcher(getActivity());
        }

        @Override
        protected String doInBackground(Void... params) {
            return syncAdapter.onPerformSync();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null)
                try {
                    syncAdapter.getCurrentWeatherDataFromJson(result, listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    public static class StoreAlertDialog extends DialogFragment {

        public static StoreAlertDialog newInstance(int title) {
            StoreAlertDialog frag = new StoreAlertDialog();
            Bundle args = new Bundle();
            args.putInt(Constants.KEY_TITLE, title);
            frag.setArguments(args);
            
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt(Constants.KEY_TITLE);

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveButton(R.string.alert_dialog_do_not_store,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((AddNewFishingActivity)getActivity()).doPositiveClick();
                                }
                            }
                    )
                    .setNegativeButton(R.string.alert_dialog_stay_here,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }
                    )
                    .create();
        }
    }

}
