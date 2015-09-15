package net.validcat.fishing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.validcat.fishing.db.Constants;
import net.validcat.fishing.fragments.ListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements ListFragment.IClickListener {
    public static final String LOG_TAG = ListActivity.class.getSimpleName();
    @Bind(R.id.fab_add_fishing)
    FloatingActionButton fab_add_fishing;

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
        // check getResources().getConfiguration().orientation

//			startActivity(new Intent(context, DetailFragment.class).putExtra("id", id));

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
