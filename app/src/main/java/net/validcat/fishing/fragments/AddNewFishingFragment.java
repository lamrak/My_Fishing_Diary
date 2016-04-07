package net.validcat.fishing.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import net.validcat.fishing.tools.TackleBag;
import net.validcat.fishing.tools.ViewAnimatorUtils;
import net.validcat.fishing.weather.WeatherSyncFetcher;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddNewFishingFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener { //, LocationListener
    public static final String LOG_TAG = AddNewFishingFragment.class.getSimpleName();
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.et_place)
    EditText etPlace;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.weather_holder)
    LinearLayout weatherWrapper;
    @Bind(R.id.tv_weather)
    TextView tvWeather;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.et_details)
    EditText etDetails;
    @Bind(R.id.iv_weather)
    ImageView ivWeather;
    @Bind(R.id.et_bait)
    EditText etBait;
    @Bind(R.id.et_fish_feed)
    EditText etFishFeed;
    @Bind(R.id.tv_tackle_value)
    TextView tvTackleValue;
//    @Bind(R.id.et_catch) EditText etCatch;

    //tackle
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

    private CameraManager cm;
    private Uri uri;
    private FishingItem item;
    private boolean updateData = false;
    //weather
    private int weatherIconSelection = 0;
    private int weatherTemp = 0;
    private String photoPath;
    private String photoId;
    private long date = 0;
    private TackleBag tacklesBag;

    private GoogleApiClient mGoogleApiClient;
    private double latitude;
    private double longitude;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private boolean isWeatherRequestDone = false;
    MapFragment mMapFragment;
    GoogleMap mGoogleMap;
    LatLng currentLocation;

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
        weatherWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runWeatherDialog();

            }
        });

        initTackleUI();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            Log.d(LOG_TAG, "ACCESS_FINE_LOCATION is not granted");
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(LOG_TAG, "ACCESS_FINE_LOCATION is requested");
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//            }
            }
        }

        if (updateData) {
            updateWeatherData(PrefUtils.getFormattedTemp(getActivity(), weatherTemp),
                    PrefUtils.formatWeatherSeletedToIconsCode(weatherIconSelection));
        }

        cm = new CameraManager();
        //Google API init
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        // attach map fragment to map_holder, get reference.
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_holder, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                loadMap(googleMap);

            }
        });

        return addNewFragmentView;
    }

    private void loadMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (updateData)
            return;
        getCurrentLocation();
        makeWeatherRequest();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
        Log.e(LOG_TAG, "onConnectionSuspended");

        makeWeatherRequest();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }

        makeWeatherRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mResolvingError && !isWeatherRequestDone) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

        tacklesBag = new TackleBag(getResources().getStringArray(R.array.tackle_array));
    }

    @Override
    public void onClick(View v) {
        v.setSelected(!v.isSelected());
        switch (v.getId()) {
            case R.id.ic_rod:
                tacklesBag.handle(0);
                break;
            case R.id.ic_spinning:
                tacklesBag.handle(1);
                break;
            case R.id.ic_feeder:
                tacklesBag.handle(2);
                break;
            case R.id.ic_distance_casting:
                tacklesBag.handle(3);
                break;
            case R.id.ic_ice_fishing_rod:
                tacklesBag.handle(4);
                break;
            case R.id.ic_tip_up:
                tacklesBag.handle(5);
                break;
            case R.id.ic_hand_line:
                tacklesBag.handle(6);
                break;
            case R.id.ic_fly_fishing:
                tacklesBag.handle(7);
                break;
            default:
                return;
        }

//        counter.start();
        if (!animTackleViewState) {
            ViewAnimatorUtils.expand(tvTackleValue);
            animTackleViewState = true;
        }
        updateTextView();
    }

    //animation for tackles
//    CountDownTimer counter = new CountDownTimer(5000, 1000) {
//
//        public void onTick(long millisUntilFinished) {}
//
//        public void onFinish() {
//            ViewAnimatorUtils.collapse(tvTackleValue);
//            animTackleViewState = false;
//        }
//    };

    boolean animTackleViewState = false;

    private void updateTextView() {
        tvTackleValue.setText(tacklesBag.getSelectedTackles());
    }

    public void makeWeatherRequest() {
        isWeatherRequestDone = true;
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
            cv.put(FishingEntry.COLUMN_LATITUDE, currentLocation.latitude);
            cv.put(FishingEntry.COLUMN_LONGITUDE, currentLocation.longitude);
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
            case Constants.PICK_PHOTO:
                ivPhoto.setVisibility(View.VISIBLE);
                //http://stackoverflow.com/questions/20260416/retrieve-absolute-path-when-select-image-from-gallery-kitkat-android
                Uri selectedImage = Uri.parse(data.getStringExtra(Constants.IMAGE_URI));
                photoId = CameraManager.getPhotoIdFromUri(getActivity(), selectedImage);
                photoPath = CameraManager.getPath(getActivity(), selectedImage);
                CameraManager.setPic(photoPath, ivPhoto);
//                setImage(selectedImage);
                break;
            case REQUEST_RESOLVE_ERROR:
                mResolvingError = false;
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();
                break;
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

            currentLocation = new LatLng(cursor.getDouble(FishingContract.FishingEntry.INDEX_LATITUDE),
                    cursor.getDouble(FishingContract.FishingEntry.INDEX_LONGITUDE));

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

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            currentLocation = new LatLng(location.getLatitude(),location.getLongitude());

            //Log.d(LOG_TAG, "lat" + latitude + "long" + longitude );

        // add draggable marker
            mGoogleMap.addMarker(new MarkerOptions()
                    .title(getResources().getString(R.string.map_place))
                    .draggable(true)
                    .position(currentLocation));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {}

                @Override
                public void onMarkerDrag(Marker marker) {}

                @Override
                public void onMarkerDragEnd(Marker marker) {
                   currentLocation =  marker.getPosition();
                   mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                }
            });
        }

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
            return syncAdapter.onPerformSync(latitude, longitude);
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

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((AddNewFishingActivity)getActivity()).onDialogDismissed();
        }
    }

}
