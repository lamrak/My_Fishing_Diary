package net.validcat.fishing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import net.validcat.fishing.db.DB;
import java.util.Calendar;

public class AddNewFishing extends Activity implements OnClickListener {
    public static final String LOG_TAG = "myLogs";
    private EditText etPlace;
    private EditText etDate;
    private EditText etWeather;
    private EditText etProcess;
    private EditText etCatch;
    private Button btnCreate;
    private Button btnChange;
    DB db;
    int DIALOG_DATE = 1;
    int myDay;
    int myMonth;
    int myYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fishing_list);

        // find items
        etPlace = (EditText) findViewById(R.id.etPlace);
        etDate = (EditText) findViewById(R.id.etDate);
        etWeather = (EditText) findViewById(R.id.etWeather);
        etProcess = (EditText) findViewById(R.id.etProcess);
        etCatch = (EditText) findViewById(R.id.etCatch);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnChange = (Button) findViewById(R.id.btnChange);

        // listener for the button
        btnCreate.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        Intent MyIntent = getIntent();
        String date = MyIntent.getStringExtra("keyDate");
        // Log.d(LOG_TAG, " --- MyDate --- " + date);
        etDate.setText(date);

        Calendar c = Calendar.getInstance();
        myDay = c.get(Calendar.DAY_OF_MONTH);
        myMonth = c.get(Calendar.MONTH);
        myYear = c.get (Calendar.YEAR);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                // save in data base
                String myPlace = etPlace.getText().toString();
                String myDate = etDate.getText().toString();
                String myWeather = etWeather.getText().toString();
                String myDescription = etProcess.getText().toString();
                String myCatch = etCatch.getText().toString();

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
                FishingItem.packageIntent(data, myPlace, myDate, id);
                // send container
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btnChange:
                showDialog(DIALOG_DATE);
        }


    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog dpd = new DatePickerDialog(this, myCallBack,
                   myYear, myMonth, myDay);
            return dpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                             int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear;
            myDay = dayOfMonth;
            etDate.setText(myDay + "." + myMonth + "." + myYear);
        }
    };
}
