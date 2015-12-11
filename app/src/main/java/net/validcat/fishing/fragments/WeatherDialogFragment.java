package net.validcat.fishing.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import net.validcat.fishing.R;

/**
 * Created by Denis on 10.12.2015.
 */
public class WeatherDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_weather,null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.title_weather_dialog)
                .setPositiveButton(android.R.string.ok,null)
                .setNegativeButton(android.R.string.no,null)
                .create();
    }
}
