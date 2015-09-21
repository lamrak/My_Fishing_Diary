package net.validcat.fishing;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.validcat.fishing.db.Constants;
import net.validcat.fishing.fragments.DetailFragment;
import net.validcat.fishing.fragments.ListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements ListFragment.IClickListener {
    public static final String LOG_TAG = ListActivity.class.getSimpleName();
    @Bind(R.id.fab_add_fishing)
    FloatingActionButton fab_add_fishing;
    boolean panel;
    String TAG = "detail_fragment";

//    private ImageView imageViewRound;
//    @Bind(R.id.my_recycler_view) RecyclerView recyclerView;

//    private RecyclerView.Adapter adapter;
//    List<FishingItem> itemsList = new ArrayList<FishingItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        ButterKnife.bind(this);
        fab_add_fishing.setOnClickListener(new View.OnClickListener() {
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
            args.putLong("fragment", clickedItemId);
            Log.d(LOG_TAG, "args = " + args);
            Fragment df = new DetailFragment();
            df.setArguments(args);
            if (args == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, df, TAG).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, df, TAG).commit();
              }
            //  get updatUIbyItenId

//        if (twoPane) {
//            Bundle args = new Bundle();
//            args.putParcelable(MovieDetailFragment.DETAIL_URI, uri);
//            MovieDetailFragment fragment = new MovieDetailFragment();
//            fragment.setArguments(args);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.movie_detail_container, fragment, F_DETAIL_TAG)
//                    .commit();
//        } else startActivity(new Intent(this, MovieDetailActivity.class).setData(uri));
          }
    }

    public boolean getPanelOrientation() {
        return panel;
    }
}
