package net.validcat.fishing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.validcat.fishing.data.Constants;
import net.validcat.fishing.fragments.AddNewFishingFragment;

public class AddNewFishingActivity extends AppCompatActivity {
    public static final String LOG_TAG = AddNewFishingActivity.class.getSimpleName();
    private AddNewFishingFragment anff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_fishing_activity);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AddNewFishingFragment anff = //TODO check this
                (AddNewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
        anff.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        anff = (AddNewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    anff.runCamera();
                } else {
                    Toast.makeText(this, R.string.camera_permissoin_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    anff.getCurrentLocation();
                }
                anff.makeWeatherRequest();
                break;
            }
            case Constants.PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
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
        anff = (AddNewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
        anff.onDialogDismissed();
    }
}

