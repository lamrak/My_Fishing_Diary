package net.validcat.fishing;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.validcat.fishing.data.Constants;
import net.validcat.fishing.fragments.DetailFragment;
import net.validcat.fishing.fragments.ListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements ListFragment.IClickListener {
    public static final String LOG_TAG = ListActivity.class.getSimpleName();
    public static final String KEY_CLICKED_FRAGMENT = "clicked_fragment";
    @Bind(R.id.fab_add_fishing)
    FloatingActionButton fabAddFishing;

     boolean panel;
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

//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        Transition transition = getWindow().setSharedElementEnterTransition(transition);
//        getWindow().setSharedElementExitTransition(transition);

    }



    @Override
    public void onItemClicked(long clickedItemId, View... sharedView) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            panel = true;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                //noinspection unchecked
                startActivity(new Intent(ListActivity.this, DetailActivity.class)
                        .putExtra(Constants.DETAIL_KEY, clickedItemId),
                        ActivityOptions.makeSceneTransitionAnimation(this,
                                new Pair<>(sharedView[0], sharedView[0].getTransitionName()),
                                new Pair<>(sharedView[1], sharedView[1].getTransitionName()),
                                new Pair<>(sharedView[2], sharedView[2].getTransitionName())).toBundle());
            } else startActivity(new Intent(ListActivity.this, DetailActivity.class).putExtra(Constants.DETAIL_KEY, clickedItemId));
        } else {
            panel = false;
            Bundle args = new Bundle();
            args.putLong(KEY_CLICKED_FRAGMENT, clickedItemId);

            Fragment df = new DetailFragment();
            df.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, df, TAG).commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
            break;
            default:
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }



}