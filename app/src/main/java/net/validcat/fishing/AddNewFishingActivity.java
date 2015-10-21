package net.validcat.fishing;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.db.DB;
import net.validcat.fishing.tools.CameraManager;

import java.io.ByteArrayOutputStream;

public class AddNewFishingActivity extends AppCompatActivity {
    public static final String LOG_TAG = AddNewFishingActivity.class.getSimpleName();
    private DB db;
    private CameraManager cm;
    ImageView ivPhoto;
    byte[] photo;
    Bitmap myPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_fishing_activity);

        assert getSupportActionBar() != null;
        AppCompatActivity appCompatActivity = (AppCompatActivity) this;
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
        EditText etPlace = (EditText) anff.getView().findViewById(R.id.et_place);
        TextView tvDate = (TextView) anff.getView().findViewById(R.id.tv_date);
        TextView tvWeather = (TextView) anff.getView().findViewById(R.id.tv_weather);
        EditText etDetails = (EditText) anff.getView().findViewById(R.id.et_details);
        EditText etPrice = (EditText) anff.getView().findViewById(R.id.et_price);
        ivPhoto = (ImageView) anff.getView().findViewById(R.id.iv_photo);

        switch (item.getItemId()) {
            case R.id.action_send:
                String myPlace = etPlace.getText().toString();
                String myDate = tvDate.getText().toString();
                String myWeather = tvWeather.getText().toString();
                String myDescription = etDetails.getText().toString();
                String myPrice = etPrice.getText().toString();

                BitmapDrawable drawable = (BitmapDrawable) ivPhoto.getDrawable();
                myPhoto = drawable.getBitmap();
                Log.d(LOG_TAG, "myPhoto =" + myPhoto);
                photo = getByteArrayfromBitmap(myPhoto);

                FishingItem items = new FishingItem();
                items.setPlace(myPlace);
                items.setDate(myDate);
                items.setWeather(myWeather);
                items.setDescription(myDescription);
                items.setPrice(myPrice);
                if (photo != null) {
                    items.setPhoto(photo);
                } else {
                    Log.d(LOG_TAG, "photo == null");
                }

                // open a connection to the database
                db = new DB(this);
                db.open();
                long id = db.saveFishingItem(items);
                db.close();

                Intent data = new Intent();
                FishingItem.packageIntent(data, myPlace, myDate, id, myDescription, myPhoto);
                // send container
                setResult(RESULT_OK, data);
                finish();
                break;

            case R.id.action_camera:
                cm = new CameraManager();
                cm.startCameraForResult(this);
                break;
        }

        return super.

                onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap b = cm.getCameraPhoto(this);
        b = cm.scaleDownBitmap(b,200,this);
        if (b != null) {
            ivPhoto.setImageBitmap(b);
            Log.d(LOG_TAG, "Intent data onActivityResult == " +b);
        } else {
            Log.d(LOG_TAG, "Intent data onActivityResult == null");
        }
    }

    public byte[] getByteArrayfromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
//        toByteArray() - Returns the contents of this ByteArrayOutputStream as a byte array.
        return bos.toByteArray();
    }
}

