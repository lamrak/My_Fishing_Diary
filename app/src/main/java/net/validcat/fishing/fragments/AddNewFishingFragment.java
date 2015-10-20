package net.validcat.fishing.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.db.DB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Denis on 11.09.2015.
 */
public class AddNewFishingFragment extends Fragment implements View.OnClickListener {

    public AddNewFishingFragment() {
    }

//    @Bind(R.id.et_place)
//    EditText etPlace;
      @Bind(R.id.tv_date)
      TextView tvDate;
//    @Bind(R.id.tv_weather)
//    TextView tvWeather;
//    @Bind(R.id.et_price)
//    EditText etPrice;
//    @Bind(R.id.et_details)
//    EditText etDetails;
//    @Bind(R.id.fab_add_fishing_list)
//    FloatingActionButton fab_add_fishing_list;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    private Bitmap bitmap;
    private DB db;
    private static final int DIALOG_DATE = 1;
    private int day;
    private int month;
    private int year;
    //private CameraManager cm;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View addNewFragmentView = inflater.inflate(R.layout.add_new_fishing_fragment, container, false);
        ButterKnife.bind(this, addNewFragmentView);

       // fab_add_fishing_list.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date(System.currentTimeMillis()));

        tvDate.setText(date);

        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        return addNewFragmentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.fab_add_fishing_list:
//                // save in data base
//                String myPlace = etPlace.getText().toString();
//                String myDate = tvDate.getText().toString();
//                String myWeather = tvWeather.getText().toString();
//                String myDescription = etDetails.getText().toString();
//                String myPrice = etPrice.getText().toString();
//
//                FishingItem item = new FishingItem();
//                item.setPlace(myPlace);
//                item.setDate(myDate);
//                item.setWeather(myWeather);
//                item.setDescription(myDescription);
//                item.setPrice(myPrice);
//
//                // open a connection to the database
//                db = new DB(getActivity());
//                db.open();
//                long id = db.saveFishingItem(item);
//                db.close();
//
//                Intent data = new Intent();
//                FishingItem.packageIntent(data, myPlace, myDate, id, myDescription);
//                // send container
//                getActivity().setResult(Activity.RESULT_OK, data);
//                getActivity().finish();
//                break;
            case R.id.tv_date:
                DialogFragment picker = new DatePickerFragment(tvDate);
                picker.show(getFragmentManager(),"datePicker");
                break;
//            case R.id.iv_photo:
//                cm = new CameraManager();
//                cm.startCameraForResult(getActivity());
//                break;
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Bitmap b = cm.extractPhotoBitmapFromResult(requestCode, resultCode, data);
//        if (b != null) {
//            ivPhoto.setImageBitmap(b);
//        }
//    }


}