package net.validcat.fishing.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.models.Fishing;
import net.validcat.fishing.models.User;
import net.validcat.fishing.tools.DateUtils;
import net.validcat.fishing.tools.PrefUtils;
import net.validcat.fishing.tools.TackleBag;
import net.validcat.fishing.tools.ViewAnimatorUtils;
import net.validcat.fishing.weather.WeatherSyncFetcher;

import org.json.JSONException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewFishingFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private static final String REQUIRED = "Required";
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";

    @Bind(R.id.weather_holder)
    LinearLayout weatherWrapper;

    @Bind(R.id.et_bait)
    EditText etBait;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.et_details)
    EditText etDetails;
    @Bind(R.id.et_fish_feed)
    EditText etFishFeed;
    @Bind(R.id.et_place)
    EditText etPlace;

    @Bind(R.id.iv_weather)
    ImageView ivWeather;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_tackle_value)
    TextView tvTackleValue;
    @Bind(R.id.tv_weather)
    TextView tvWeather;

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

    private GoogleApiClient mGoogleApiClient;
    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    private DatabaseReference mDatabase;
    long date;
    private int weatherIconSelection = 0;
    private int weatherTemp = 0;
    private boolean isWeatherRequestDone = false;
    private boolean mResolvingError = false;
    private boolean updateData = false;
    boolean animTackleViewState = false;
    private TackleBag tacklesBag;
    private double latitude;
    private double longitude;
    LatLng currentLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_fishing_fragment, container, false);
        ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setHasOptionsMenu(true);
        initCurrentTime();
        initMapFragment();
        requestPermission();
        initWeatherUi();
        initTackleUI();

        return view;
    }

    private void initMapFragment() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_holder, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                loadMap(googleMap);
            }
        });
    }

    private void loadMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case Constants.REQUEST_CODE_PHOTO:

                break;
            case Constants.REQUEST_TEMPERATURE:
                weatherIconSelection = data.getIntExtra(Constants.EXTRA_IMAGE_KEY, -1);
                weatherTemp = data.getIntExtra(Constants.EXTRA_TEMPERATURE, 0);
                updateWeatherData(PrefUtils.getFormattedTemp(getActivity(), weatherTemp),
                        PrefUtils.formatWeatherSeletedToIconsCode(weatherIconSelection));
                break;
            case Constants.REQUEST_TAKE_PHOTO:

                break;
            case Constants.PICK_PHOTO:

                break;
            case REQUEST_RESOLVE_ERROR:
                mResolvingError = false;
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();
                break;
        }
    }
    ///////////////////////////////////////// Tackle ///////////////////////////////////////////////
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

        if (!animTackleViewState) {
            ViewAnimatorUtils.expand(tvTackleValue);
            animTackleViewState = true;
        }
        updateTextView();
    }

    private void updateTextView() {
        tvTackleValue.setText(tacklesBag.getSelectedTackles());
    }
    ///////////////////////////////////////// End Tackle ///////////////////////////////////////////


    ////////////////////////////////////////// Weather View ////////////////////////////////////////
    private void initWeatherUi() {
        weatherWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runWeatherDialog();
            }
        });
        if (updateData) {
            updateWeatherData(PrefUtils.getFormattedTemp(getActivity(), weatherTemp),
                    PrefUtils.formatWeatherSeletedToIconsCode(weatherIconSelection));
        }
    }

    private void runWeatherDialog() {
        FragmentManager fm = getActivity().getFragmentManager();
        WeatherDialogFragment weatherDialog = new WeatherDialogFragment();
        weatherDialog.setTargetFragment(NewFishingFragment.this, Constants.REQUEST_TEMPERATURE);

        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_IMAGE_KEY, weatherIconSelection);
        args.putInt(Constants.EXTRA_TEMPERATURE, weatherTemp);
        weatherDialog.setArguments(args);

        weatherDialog.show(fm, Constants.DIALOG_KEY);
    }
    private void updateWeatherData(String temp, int weather) {
        tvWeather.setText(temp);
        ivWeather.setImageResource(weather);
    }
    ////////////////////////////////////////// End Weather View ////////////////////////////////////

    ///////////////////////////////////////// Menu /////////////////////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_new_fishing_action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                break;
            case R.id.action_add_new_fishing:
                submitFishing();
                break;
            case R.id.action_camera:

                break;
            case R.id.action_settings:

                break;
            case R.id.action_modify_things_list:

                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }
    ///////////////////////////////////////// End Menu /////////////////////////////////////////////

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    private void submitFishing() {

        if (TextUtils.isEmpty(etPlace.getText().toString())) {
            etPlace.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(etDetails.getText().toString())) {
            etDetails.setError(REQUIRED);
            return;
        }

        final Fishing fishing = retrieveDataFromView();
        setEditingEnabled(false);

        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(getActivity(), "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                }else {
                    writeNewPost(userId, user.username, fishing);
                }
                setEditingEnabled(true);
                getActivity().finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                setEditingEnabled(true);
            }
        });

    }

    private Fishing retrieveDataFromView() {
        final String place = etPlace.getText().toString();
        final String details = etDetails.getText().toString();
        // TODO: 21.03.17 add another data/
        return new Fishing("","", place, date, details);
    }

    private void validateEditedData(String place, String details) {
        if (TextUtils.isEmpty(place)) {
            etPlace.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(details)) {
            etDetails.setError(REQUIRED);
            return;
        }
    }

    private void writeNewPost(String userId, String userName, Fishing fishing) {
        fishing.setAuthor(userName);
        fishing.setUid(userId);

        String key = mDatabase.child("fishings").push().getKey();

        Map<String, Object> postValues = fishing.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/fishings/" + key, postValues);
        childUpdates.put("/user-fishings/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

    }

    public void setEditingEnabled(boolean editingEnabled) {
        if (editingEnabled) hideProgressDialog();
        else showProgressDialog();
    }

    ///////////////////////////////////////// date Dialog //////////////////////////////////////////
    // TODO: 21.03.17 add calendar dialog.
    private void initCurrentTime() {
        date = Calendar.getInstance().getTimeInMillis();
        tvDate.setText(DateUtils.getFullFriendlyDayString(getActivity(), date));
    }
    ///////////////////////////////////////// End date Dialog //////////////////////////////////////

    /////////////////////////////////////// Goggle api /////////////////////////////////////////////
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (updateData)
            return;
        getCurrentLocation();
        makeWeatherRequest();
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            currentLocation = new LatLng(location.getLatitude(),location.getLongitude());

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
    public void onConnectionSuspended(int i) {
        makeWeatherRequest();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
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

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        AddNewFishingFragment.ErrorDialogFragment dialogFragment = new AddNewFishingFragment.ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getFragmentManager(), "errordialog");
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
    ///////////////////////////////////////// End Goggle api ///////////////////////////////////////
}
