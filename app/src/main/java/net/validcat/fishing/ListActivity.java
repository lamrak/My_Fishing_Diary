package net.validcat.fishing;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import net.validcat.fishing.db.Constants;
import net.validcat.fishing.fragments.DetailFragment;
import net.validcat.fishing.fragments.ListFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity implements ListFragment.IClickListener {
    public static final String LOG_TAG = ListActivity.class.getSimpleName();
    @Bind(R.id.fab_add_fishing)
    FloatingActionButton fab_add_fishing;
    boolean panel;
    String TAG = "detail_fragment";
  //  ActionBarDrawerToggle toggle;
     FishingAdapter adapter;
     List<FishingItem> itemsList;

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

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        toggle.setDrawerIndicatorEnabled(true);
//        drawerLayout.setDrawerListener(toggle);
//        ListView lv_navigation_drawer = (ListView)findViewById(R.id.lv_navigation_drawer);
//        lv_navigation_drawer.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[]{"Settings 1","Settings 2","Settings 3"}));

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
          }
    }

    public boolean getPanelOrientation() {
        return panel;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_activity_action_bar, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.ITEM_REQUEST) {

            Fragment lf = getSupportFragmentManager().findFragmentById(R.id.list_fragment);
            if(lf != null) {
                View myLfView = lf.getView();
                RecyclerView myRecycler = (RecyclerView) myLfView.findViewById(R.id.my_recycler_view);
                FishingItem item = new FishingItem(data);
                itemsList = ListFragment.itemsList;
                itemsList.add(item);
               // adapter = new  FishingAdapter(this, itemsList);
                adapter = ListFragment.adapter;
               // adapter.notifyDataSetChanged();
                myRecycler.setAdapter(adapter);
            }else{
                Toast.makeText(this,"Нооооль",Toast.LENGTH_LONG).show();
            }
            }
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(toggle.onOptionsItemSelected(item))
//            return true;
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        toggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        toggle.onConfigurationChanged(newConfig);
//    }
