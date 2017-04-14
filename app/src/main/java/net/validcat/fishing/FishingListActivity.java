package net.validcat.fishing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import net.validcat.fishing.data.Constants;
import net.validcat.fishing.fragments.MyFishingListFragment;
import net.validcat.fishing.fragments.MyTopFishingListFragment;
import net.validcat.fishing.fragments.RecentFishingListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FishingListActivity extends BaseActivity {

    @Bind(R.id.fab_add_fishing)
    FloatingActionButton fabAddFishing;
    @Bind(R.id.toolbar)
    public Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    public DrawerLayout drawer;
//    @Bind(R.id.nv_view)
//    public NavigationView navDrawer;
    @Bind(R.id.container)
    public ViewPager mViewPager;
    @Bind(R.id.tabs)
    public TabLayout tabLayout;

    private FragmentPagerAdapter mPagerAdapter;
    private ActionBarDrawerToggle toggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        requestPermission();

        // TODO: 25.03.17 uncomment this line for showing drawer.
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setElevation(0f);
//        setDrawerFragment();

//        toggle = setupDrawerToggle();
//        drawer.addDrawerListener(toggle);
//        setupDrawerContent(navDrawer);

        initFragmentPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        fabAddFishing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(FishingListActivity.this,
                        AddNewFishingActivity.class), Constants.ITEM_REQUEST);
            }
        });
    }

    private void initFragmentPagerAdapter() {
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new RecentFishingListFragment(),
                    new MyFishingListFragment(),
                    new MyTopFishingListFragment()
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.recent),
                    getString(R.string.heading_my_fishing),
                    getString(R.string.heading_my_top_fishing)
            };
            @Override public Fragment getItem(int position) { return mFragments[position]; }
            @Override public int getCount() { return mFragments.length; }
            @Override public CharSequence getPageTitle(int position) { return mFragmentNames[position]; }
        };
    }

    private void setDrawerFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_drawer, new GoogleSignInFragment())
                .commit();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                            // in case when in drawer will add menu items.
                        return true;
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        getSupportFragmentManager().findFragmentById(R.id.fragment_drawer)
//                .onActivityResult(requestCode, resultCode, data);
        // TODO: 20.03.17 this lines need for correct showing drawer that stored inside FishingListActivity fragment after onActivityResult().
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        toggle.syncState();
    }

    //////////////////////////////////////// Menu  /////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LogInActivity.class));
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //////////////////////////////////////// End Menu  /////////////////////////////////////////////

    //////////////////////////////////////// Permission ////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length <= 0 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, R.string.storage_permissoin_denied, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        }
    }
    //////////////////////////////////////// End Permission ////////////////////////////////////////
}