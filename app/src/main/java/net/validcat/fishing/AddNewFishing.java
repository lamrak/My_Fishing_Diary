package net.validcat.fishing;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.db.DB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddNewFishing extends AppCompatActivity implements OnClickListener {
    public static final String LOG_TAG = AddNewFishing.class.getSimpleName();

    @Bind(R.id.et_place) EditText etPlace;
    @Bind(R.id.tv_date) TextView tvDate;
    @Bind(R.id.tv_weather) TextView tvWeather;
    @Bind(R.id.et_price) EditText etPrice;
    @Bind(R.id.et_details) EditText etDetails;
    @Bind (R.id.fab_add_fishing_list) FloatingActionButton fab_add_fishing_list;
    @Bind(R.id.iv_photo) ImageView ivPhoto;

    //@Bind(R.id.btn_change) Button btnChange;
    //@Bind(R.id.btn_add_photo) Button btnAddFoto;

    private Bitmap bitmap;
    private DB db;
    private static final int DIALOG_DATE = 1;
    private int day;
    private int month;
    private int year;
    private CameraManager cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fishing_list_rev01);
        ButterKnife.bind(this);

        // listener for the button
        fab_add_fishing_list.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date(System.currentTimeMillis()));

        tvDate.setText(date);

        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_fishing_list:
                // save in data base
                String myPlace = etPlace.getText().toString();
                String myDate = tvDate.getText().toString();
                String myWeather = tvWeather.getText().toString();
                String myDescription = etDetails.getText().toString();
                String myCatch = etPrice.getText().toString();

                FishingItem item = new FishingItem();
                item.setPlace(myPlace);
                item.setDate(myDate);
                item.setWeather(myWeather);
                item.setDescription(myDescription);
                item.setCatches(myCatch);

                // open a connection to the database
                db = new DB(AddNewFishing.this);
                db.open();
                long id = db.saveFishingItem(item);
                db.close();

                Intent data = new Intent();
                FishingItem.packageIntent(data, myPlace, myDate, id, myDescription);
                // send container
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.tv_date:
                showDialog(DIALOG_DATE);
                break;
            case R.id.iv_photo:
                cm = new CameraManager();
                cm.startCameraForResult(this);
//                myCameraManager.startIntent();
//                bitmap = myCameraManager.getFoto();
        }

    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    AddNewFishing.this.year = year;
                    AddNewFishing.this.month = monthOfYear;
                    AddNewFishing.this.day = dayOfMonth;
                    tvDate.setText(AddNewFishing.this.day + "." + AddNewFishing.this.month + "." + AddNewFishing.this.year);
                }
            }, year, month, day);

            return dpd;
        }

        return super.onCreateDialog(id);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap b = cm.extractPhotoBitmapFromResult(requestCode, resultCode, data);
        if (b != null) {
            ivPhoto.setImageBitmap(b);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_fishing_action_bar, menu);

        return true;
    }
}
