package net.validcat.fishing.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import net.validcat.fishing.R;
import net.validcat.fishing.fragments.AddNewFishingFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Oleksii on 12/23/16.
 */

public class CalendarDataPickerDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                ((AddNewFishingFragment)
                                        getFragmentManager().findFragmentById(R.id.add_new_fragment))
                                        .onDateSet(datePicker, i, i1, i2);
                            }
                        }, year, month, day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                return dialog;
        }
}
