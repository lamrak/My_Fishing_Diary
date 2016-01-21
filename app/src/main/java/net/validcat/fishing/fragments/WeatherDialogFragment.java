package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.tools.PrefUtils;
import net.validcat.fishing.ui.RadioGridGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeatherDialogFragment extends DialogFragment {
    private static final String LOG_TAG = WeatherDialogFragment.class.getSimpleName();
    private int temperature;
    private boolean tempScale;
    @Bind(R.id.weather_group) RadioGridGroup weatherGroup;
    @Bind(R.id.sb_temp) SeekBar seekBar;
    @Bind(R.id.tv_temp) TextView tvTemp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FishingDialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_weather, null);
        ButterKnife.bind(this, v);
        weatherGroup.setChecked(getArguments().getInt(Constants.EXTRA_IMAGE_KEY, 0));
        tempScale = PrefUtils.isMetric(getActivity());

        if(tempScale){
            temperature = getArguments().getInt(Constants.EXTRA_TEMPERATURE, 50);
            tvTemp.setText(getString(R.string.weather_formatter, temperature));
            seekBar.setProgress(50 + temperature);
        }else{
            temperature = 32;
            tvTemp.setText(getString(R.string.weather_fahrenheit, temperature));
            seekBar.setMax(180);
            seekBar.setProgress(90);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private void progressChanged() {
                temperature = currentValue(seekBar.getProgress());
                if(tempScale) {
                    tvTemp.setText(getString(R.string.weather_formatter, temperature));
                }else {
                    tvTemp.setText(getString(R.string.weather_fahrenheit, temperature));
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressChanged();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressChanged();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.title_weather_dialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
    }

    private int currentValue(int progress) {
        if (tempScale) {
            return progress - 50;
        } else {
            return progress - 58;
        }
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(Constants.EXTRA_TEMPERATURE, temperature);
        int ind = getSelectedWeather();
        Log.d(LOG_TAG, "Selected = " + ind);
        i.putExtra(Constants.EXTRA_IMAGE_KEY, ind);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public int getSelectedWeather() {
        return weatherGroup.indexOfChild(weatherGroup.findViewById(weatherGroup.getCheckedRadioButtonId()));
    }
}
