package net.validcat.fishing.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoDialogFragment extends DialogFragment {
    private static final String LOG_TAG = PhotoDialogFragment.class.getSimpleName();
    @Bind(R.id.tv_take_photo)TextView takePhoto;
    @Bind(R.id.tv_pick_photo)TextView pickPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FishingDialog);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo, null);
        ButterKnife.bind(this, v);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK);
                getDialog().dismiss();
            }
        });
        pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Constants.PICK_PHOTO);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.title_photo_dialog)
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent takePhotoIntent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, takePhotoIntent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.PICK_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
//                    ((MainActivity) getActivity()).setImage(selectedImage);
                    Intent pickPhotoIntent = new Intent();
                    pickPhotoIntent.putExtra(Constants.IMAGE_URI, selectedImage.toString());
                    getTargetFragment().onActivityResult(Constants.PICK_PHOTO, Activity.RESULT_OK, pickPhotoIntent);
                    getDialog().dismiss();
                }
                break;
        }
    }
}

