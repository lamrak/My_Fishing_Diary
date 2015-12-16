package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;

/**
 * Created by Denis on 10.12.2015.
 */
public class WeatherDialogFragment extends DialogFragment {
    String weatherKey;
    String temperature;
    ImageView sunny;
    ImageView cloudy;
    ImageView partlyCloudy;
    ImageView rain;
    ImageView snow;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_weather,null);
        SeekBar seekBar = (SeekBar)v.findViewById(R.id.seekBar);
        final TextView temperatureValue = (TextView)v.findViewById(R.id.temperatureValue);

        sunny = (ImageView)v.findViewById(R.id.icSunny);
        cloudy = (ImageView)v.findViewById(R.id.icCloudy);
        partlyCloudy = (ImageView)v.findViewById(R.id.icPartlyCloudy);
        rain = (ImageView)v.findViewById(R.id.icRain);
        snow = (ImageView)v.findViewById(R.id.icSnow);

        View.OnClickListener oclBtn = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.icSunny:
                        sunny.setBackgroundColor(getResources().getColor(R.color.color_background));
                        cloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        partlyCloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        rain.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        snow.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        weatherKey = "Sunny";
                        break;
                    case R.id.icCloudy:
                        cloudy.setBackgroundColor(getResources().getColor(R.color.color_background));
                        sunny.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        partlyCloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        rain.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        snow.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        weatherKey = "Cloudy";
                        break;
                    case R.id.icPartlyCloudy:
                        cloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        sunny.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        partlyCloudy.setBackgroundColor(getResources().getColor(R.color.color_background));
                        rain.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        snow.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        weatherKey = "PartlyCloudy";
                        break;
                    case R.id.icRain:
                        cloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        sunny.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        partlyCloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        rain.setBackgroundColor(getResources().getColor(R.color.color_background));
                        snow.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        weatherKey = "Rain";
                        break;
                    case R.id.icSnow:
                        cloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        sunny.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        partlyCloudy.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        rain.setBackgroundColor(getResources().getColor(R.color.color_default_background));
                        snow.setBackgroundColor(getResources().getColor(R.color.color_background));
                        weatherKey = "Snow";
                        break;
                }
            }
        };
        sunny.setOnClickListener(oclBtn);
        cloudy.setOnClickListener(oclBtn);
        partlyCloudy.setOnClickListener(oclBtn);
        rain.setOnClickListener(oclBtn);
        snow.setOnClickListener(oclBtn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temperature = currentValue(seekBar.getProgress());
                temperatureValue.setText(temperature);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                temperature = currentValue(seekBar.getProgress());
                temperatureValue.setText(temperature);
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
                .setNegativeButton(android.R.string.no,null)
                .create();
    }

    private String currentValue (int progress){
        if (progress>50) {
            return "+" + String.valueOf(progress-50)+ "\u00B0" + "C" ;
        }else{
            String negative = "-" + String.valueOf(50-progress)+ "\u00B0" + "C";
           return negative;
        }
    }

    private void sendResult (int resultCode){
        if (getTargetFragment()==null)
            return;
        Intent i = new Intent();
        i.putExtra(Constants.EXTRA_TEMPERATURE,temperature);
        i.putExtra(Constants.EXTRA_IMAGE_KEY, weatherKey);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }

}
