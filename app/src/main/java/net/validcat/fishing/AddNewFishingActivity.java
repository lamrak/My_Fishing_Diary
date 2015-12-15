package net.validcat.fishing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.validcat.fishing.fragments.AddNewFishingFragment;

public class AddNewFishingActivity extends AppCompatActivity {
    public static final String LOG_TAG = AddNewFishingActivity.class.getSimpleName();
    public static final int PERMISSIONS_REQUEST_CAMERA = 191;
    public static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 191;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_fishing_activity);

        assert getSupportActionBar() != null;
        AppCompatActivity appCompatActivity = (AppCompatActivity) this;
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_revert);
        
        fetchCurrentWeather();
    }

    private void fetchCurrentWeather() {
        //TODO create AsyncTask
        //TODO fetch weather with SyncAdapter
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AddNewFishingFragment anff = //TODO check this
                (AddNewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
        anff.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    AddNewFishingFragment anff = //TODO check this
                            (AddNewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
                    anff.runCamera();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

