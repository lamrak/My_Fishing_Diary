package net.validcat.fishing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.validcat.fishing.db.DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    public static final String LOG_TAG = "FishingList";
    private static final int ITEM_REQUEST = 0;
    private RecyclerView.Adapter adapter;

    List<FishingItem> itemsList = new ArrayList<FishingItem>();

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy"); //TODO http://prntscr.com/7prdux
        final String date = sdf.format(new Date(System.currentTimeMillis()));

        initDataBase();
        initUI(date);
    }

    private void initUI(final String date) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new FishingAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fab_add_fishing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(MainActivity.this,
                        AddNewFishing.class);
                startNewActivity.putExtra("keyDate", date);
                startActivityForResult(startNewActivity, ITEM_REQUEST);
            }
        });
    }

    private void initDataBase() {
        DB db = new DB(this);
        db.open();
        Cursor cursor = db.getAllData();
        if (cursor.moveToFirst()) itemsList = db.getData(itemsList, cursor);
        else cursor.close();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ITEM_REQUEST) {
            FishingItem item = new FishingItem(data);
            itemsList.add(item);
            adapter.notifyDataSetChanged();
        }
    }

}
