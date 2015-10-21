package net.validcat.fishing;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import net.validcat.fishing.db.Constants;
import net.validcat.fishing.fragments.DetailFragment;
import net.validcat.fishing.fragments.ListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements ListFragment.IClickListener {
    public static final String LOG_TAG = ListActivity.class.getSimpleName();
    private static final String KEY_CLICKED_FRAGMENT = "clicked_fragment";
    @Bind(R.id.fab_add_fishing)
    private FloatingActionButton fabAddFishing;

    private boolean panel;
    private  static final String TAG = "detail_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        ButterKnife.bind(this);
        fabAddFishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ListActivity.this, AddNewFishingActivity.class), Constants.ITEM_REQUEST);
            }
        });
    }

    @Override
    public void onItemClicked(long clickedItemId) {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            panel = true;
            startActivity(new Intent(ListActivity.this, DetailActivity.class).putExtra("id", clickedItemId));
        } else {
            panel = false;
            Bundle args = new Bundle();
            args.putLong(KEY_CLICKED_FRAGMENT, clickedItemId);

            Fragment df = new DetailFragment();
            df.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, df, TAG).commit(); // add?
          }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_activity_action_bar, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.ITEM_REQUEST) {
            ListFragment lf = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
            if(lf != null)
                lf.addNewItem(data);
        }
    }
}