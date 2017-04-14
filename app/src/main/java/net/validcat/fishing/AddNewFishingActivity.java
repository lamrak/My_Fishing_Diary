package net.validcat.fishing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import net.validcat.fishing.data.Constants;
import net.validcat.fishing.fragments.NewFishingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddNewFishingActivity extends BaseActivity {
    public static final String LOG_TAG = AddNewFishingActivity.class.getSimpleName();
    private NewFishingFragment fragment;

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_fishing_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        NewFishingFragment fragment  =
                (NewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        fragment = (NewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    fragment.runCamera();
                } else {
                    Toast.makeText(this, R.string.camera_permissoin_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    fragment.getCurrentLocation();
                }
                fragment.makeWeatherRequest();
                break;
            }
            case Constants.PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length <= 0 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, R.string.storage_permissoin_denied, Toast.LENGTH_SHORT).show();
                        }
                break;
            }
        }
    }

    public void doPositiveClick() {
        finish();
    }

    public void onDialogDismissed() {
        fragment = (NewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
//        fragment.onDialogDismissed();
    }
}

