package net.validcat.fishing.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.validcat.fishing.R;
import net.validcat.fishing.adapters.ContactsAdapter;
import net.validcat.fishing.adapters.IRecyclerViewClickListener;
import net.validcat.fishing.data.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InviteFriendsDialogFragment extends DialogFragment implements IRecyclerViewClickListener {
    private static final String LOG_TAG = InviteFriendsDialogFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private RecyclerView rv;
    private ContactsAdapter adapter;
    private List<String> selectedContacts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedContacts = new ArrayList<>();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.invite_friends_dialog, null);
        rv = ((RecyclerView) v.findViewById(R.id.contacts_list_recycler_view));
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            initAdapter();
        }
        ((Button) (v.findViewById(R.id.send_mail_button))).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendInvitation(selectedContacts);
                    }
                }
        );
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.title_friends_dialog)
                .create();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initAdapter();
            } else {
                dismiss();
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initAdapter() {
        adapter = new ContactsAdapter(getActivity(), this);
        rv.setAdapter(adapter);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        selectedContacts.add(((TextView) (v.findViewById(android.R.id.text2))).getText().toString());
        v.setSelected(true);
    }

    private void sendInvitation(List<String> emails) {
        String[] recipients = new String[emails.size()];
        for (int i = 0; i < emails.size(); i++) {
            recipients[i] = emails.get(i);
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , recipients);
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}

