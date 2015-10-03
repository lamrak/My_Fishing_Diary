package net.validcat.fishing;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import net.validcat.fishing.db.DB;

public class AddNewFishingActivity extends AppCompatActivity {
    public static final String LOG_TAG = AddNewFishingActivity.class.getSimpleName();
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_fishing_activity);

        assert getSupportActionBar() != null;
        AppCompatActivity appCompatActivity = (AppCompatActivity)this;
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_revert);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_fishing_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment anff = getFragmentManager().findFragmentById(R.id.add_new_fragment);
        EditText etPlace = (EditText)anff.getView().findViewById(R.id.et_place);
        TextView tvDate = (TextView)anff.getView().findViewById(R.id.tv_date);
        TextView tvWeather = (TextView)anff.getView().findViewById(R.id.tv_weather);
        EditText etDetails = (EditText)anff.getView().findViewById(R.id.et_details);
        EditText etPrice = (EditText)anff.getView().findViewById(R.id.et_price);
        switch (item.getItemId()) {
            case R.id.action_send:
                String myPlace = etPlace.getText().toString();
                String myDate = tvDate.getText().toString();
                String myWeather = tvWeather.getText().toString();
                String myDescription = etDetails.getText().toString();
                String myPrice = etPrice.getText().toString();

                FishingItem items = new FishingItem();
                items.setPlace(myPlace);
                items.setDate(myDate);
                items.setWeather(myWeather);
                items.setDescription(myDescription);
                items.setPrice(myPrice);

                // open a connection to the database
                db = new DB(this);
                db.open();
                long id = db.saveFishingItem(items);
                db.close();

                Intent data = new Intent();
                FishingItem.packageIntent(data, myPlace, myDate, id, myDescription);
                // send container
                setResult(RESULT_OK, data);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

