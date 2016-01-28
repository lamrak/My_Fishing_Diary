package net.validcat.fishing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.validcat.fishing.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {
    public static final int EDIT_ID = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
    }

    public void doPositiveClick() {
        DetailFragment detail
                = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
        detail.deleteFishingItem();
    }

}
