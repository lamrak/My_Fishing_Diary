package net.validcat.fishing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.validcat.fishing.fragments.AddNewFishingFragment;

public class AddNewFishingActivity extends AppCompatActivity {
    public static final String LOG_TAG = AddNewFishingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_fishing_activity);

        assert getSupportActionBar() != null;
        AppCompatActivity appCompatActivity = (AppCompatActivity) this;
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_revert);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AddNewFishingFragment anff = //TODO check this
                (AddNewFishingFragment) getFragmentManager().findFragmentById(R.id.add_new_fragment);
        anff.onActivityResult(requestCode, resultCode, data);
    }
}

