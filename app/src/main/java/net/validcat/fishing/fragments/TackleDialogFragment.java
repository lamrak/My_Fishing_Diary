package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.ui.RadioGridGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TackleDialogFragment  extends DialogFragment {
    private static final String LOG_TAG = TackleDialogFragment.class.getSimpleName();
    @Bind(R.id.tackle_group) RadioGridGroup tackleGroup;
    @Bind(R.id.tv_tackle_value) TextView tvTackleValue;
    int ind = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FishingDialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_tackle, null);
        ButterKnife.bind(this, v);

        View rod = tackleGroup.findViewById(R.id.ic_rod);
        View spinning = tackleGroup.findViewById(R.id.ic_spinning);
        View feeder = tackleGroup.findViewById(R.id.ic_feeder);
        View distance_casting = tackleGroup.findViewById(R.id.ic_distance_casting);
        View ice_fishing_rod = tackleGroup.findViewById(R.id.ic_ice_fishing_rod);
        View tip_up = tackleGroup.findViewById(R.id.ic_tip_up);
        View hand_line = tackleGroup.findViewById(R.id.ic_hand_line);
        View fly_fishing = tackleGroup.findViewById(R.id.ic_fly_fishing);

        View.OnClickListener clickIcon = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ic_rod:
                        tvTackleValue.setText(R.string.rod);
                        ind = 0;
                        break;
                    case R.id.ic_spinning:
                        tvTackleValue.setText(R.string.spinning);
                        ind = 1;
                        break;
                    case R.id.ic_feeder:
                        tvTackleValue.setText(R.string.feeder);
                        ind = 2;
                        break;
                    case R.id.ic_distance_casting:
                        tvTackleValue.setText(R.string.distance_casting);
                        ind = 3;
                        break;
                    case R.id.ic_ice_fishing_rod:
                        tvTackleValue.setText(R.string.ice_fishing_rod);
                        ind = 4;
                        break;
                    case R.id.ic_tip_up:
                        tvTackleValue.setText(R.string.tip_up);
                        ind = 5;
                        break;
                    case R.id.ic_hand_line:
                        tvTackleValue.setText(R.string.hand_line);
                        ind = 6;
                        break;
                    case R.id.ic_fly_fishing:
                        tvTackleValue.setText(R.string.fly_fishing);
                        ind = 7;
                        break;
                }
            }
        };

        rod.setOnClickListener(clickIcon);
        spinning.setOnClickListener(clickIcon);
        feeder.setOnClickListener(clickIcon);
        distance_casting.setOnClickListener(clickIcon);
        ice_fishing_rod.setOnClickListener(clickIcon);
        tip_up.setOnClickListener(clickIcon);
        hand_line.setOnClickListener(clickIcon);
        fly_fishing.setOnClickListener(clickIcon);

//        rod.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvTackleValue.setText(R.string.rod);
//                ind = 0;
//            }
//        });
//
//        spinning.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvTackleValue.setText(R.string.spinning);
//                ind = 1;
//            }
//        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.title_tackle_dialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
    }


    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
       // int ind = getSelectedTackle();
        //Log.d(LOG_TAG, "Selected = " + ind);
        i.putExtra(Constants.EXTRA_TACKLE_IMAGE_KEY, ind);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

//    public int getSelectedTackle() {
//            return tackleGroup.indexOfChild(tackleGroup.findViewById(tackleGroup.getCheckedRadioButtonId()));
//    }

}
