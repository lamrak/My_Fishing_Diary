package net.validcat.fishing.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import net.validcat.fishing.AddNewFishingActivity;
import net.validcat.fishing.FishingItem;
import net.validcat.fishing.ListActivity;
import net.validcat.fishing.R;
import net.validcat.fishing.db.Constants;
import net.validcat.fishing.db.DB;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {
    public static final String LOG_TAG = DetailFragment.class.getSimpleName();
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_weather)
    TextView tvWeather;
    @Bind(R.id.tv_description)
    TextView tvDescription;
    @Bind(R.id.tv_catch)
    TextView tvCatch;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    private DB db;
    Bitmap no_photo;
    long id;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailFragmentView = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, detailFragmentView);

        Bundle arguments = getArguments();
        if (arguments == null) {
            Intent intent = getActivity().getIntent();
            id = intent.getLongExtra("id", -1);
            db = new DB(getActivity());
            updateUiByItemId(id);

        } else {
            long landId = arguments.getLong(ListActivity.KEY_CLICKED_FRAGMENT);
            Log.d(LOG_TAG, "landId = " + landId);
            db = new DB(getActivity());
            updateUiByItemId(landId);
        }

        return detailFragmentView;
    }

    public void updateUiByItemId(long id) {
        db.open();
        FishingItem item = db.getFishingItemById(id);
        db.close();
        tvPlace.setText(getString(R.string.fishing_place, item.getPlace()));
        tvPlace.setContentDescription(getString(R.string.fishing_place, item.getPlace()));
        tvDate.setText(getString(R.string.fishing_date, item.getDate()));
        tvDate.setContentDescription(getString(R.string.fishing_date, item.getDate()));
        tvWeather.setText(item.getWeather());
//        tvWeather.setText(getString(R.string.fishing_weather, item.getWeather()));
//        tvWeather.setContentDescription(getString(R.string.fishing_weather, item.getWeather()));
        tvDescription.setText(getString(R.string.fishing_description, item.getDescription()));
        tvDescription.setContentDescription(getString(R.string.fishing_description, item.getDescription()));
        tvCatch.setText(getString(R.string.fishing_price, item.getPrice()));
        tvCatch.setContentDescription(getString(R.string.fishing_price, item.getPrice()));
        byte[] photo = item.getPhoto();
        if (photo != null) {
            Log.d(LOG_TAG, "photo !=null " + photo);
            ivPhoto.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
        } else {
            Log.d(LOG_TAG, "photo == null");
            no_photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_photo);
            ivPhoto.setImageBitmap(no_photo);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View menuItemView = getActivity().findViewById(R.id.settings_buton);
        PopupMenu popupMenu = new PopupMenu(getActivity(),menuItemView);
        popupMenu.inflate(R.menu.item_detail);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        // Toast.makeText(getActivity(),"Вы выбрали Редактирование",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), AddNewFishingActivity.class);
                        intent.putExtra(Constants.DETAIL_KEY, id);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        return true;
    }

    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
//        private TextView tvDate;
        String date;
//
//        public DatePickerFragment(TextView tvDate) {
//            this.tvDate = tvDate;
//        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            date = Integer.toString(day) + "." + Integer.toString(month + 1) + "." + Integer.toString(year);
            tvDate.setText(date);
        }
    }

}