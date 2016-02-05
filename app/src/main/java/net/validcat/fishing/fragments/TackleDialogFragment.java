package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TackleDialogFragment  extends DialogFragment implements View.OnClickListener {
    private static final String LOG_TAG = TackleDialogFragment.class.getSimpleName();

    @Bind(R.id.tv_tackle_value) TextView tvTackleValue;
    @Bind(R.id.ic_rod) Button rod;
    @Bind(R.id.ic_spinning) Button spinning;
    @Bind(R.id.ic_feeder) Button feeder;
    @Bind(R.id.ic_distance_casting) Button casting;
    @Bind(R.id.ic_ice_fishing_rod) Button iceRod;
    @Bind(R.id.ic_tip_up) Button tipUp;
    @Bind(R.id.ic_hand_line) Button handLine;
    @Bind(R.id.ic_fly_fishing) Button flyFishing;

    private int[] selectedIdx;
    private String[] tackles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FishingDialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_tackle, null);
        ButterKnife.bind(this, v);

        tackles = getResources().getStringArray(R.array.tackle_array);
        selectedIdx = new int[tackles.length];
//
        rod.setOnClickListener(this);
        spinning.setOnClickListener(this);
        feeder.setOnClickListener(this);
        casting.setOnClickListener(this);
        iceRod.setOnClickListener(this);
        tipUp.setOnClickListener(this);
        handLine.setOnClickListener(this);
        flyFishing.setOnClickListener(this);

        v.setBackgroundResource(android.R.color.white);
        Dialog dialog = new AlertDialog.Builder(getActivity())
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        return dialog;
    }


    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
       // int ind = getSelectedTackle();
        //Log.d(LOG_TAG, "Selected = " + ind);
        i.putExtra(Constants.EXTRA_TACKLE_IMAGE_KEY, selectedIdx);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public void onClick(View v) {
        v.setSelected(!v.isSelected());
        int ind = -1;
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

        if (ind == -1) {
            return;
        }
        selectedIdx[ind] = selectedIdx[ind] == 0 ? 1 : 0;
        updateTextView();
    }

    private void updateTextView() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < selectedIdx.length; i++) {
            if (selectedIdx[i] == 1) {
                sb.append(tackles[i]);
                sb.append(", ");
            }
        }

        tvTackleValue.setText(sb.substring(0, sb.length()-2));
    }

}
