package net.validcat.fishing.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.validcat.fishing.R;
import net.validcat.fishing.adapters.CommentAdapter;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.models.Comment;
import net.validcat.fishing.models.Fishing;
import net.validcat.fishing.models.User;
import net.validcat.fishing.tools.DateUtils;
import net.validcat.fishing.tools.PrefUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class FishingDeatailFragment extends Fragment implements OnMapReadyCallback ,View.OnClickListener{

    public static final String EXTRA_POST_KEY = "post_key";

    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_weather)
    TextView tvWeather;
    @Bind(R.id.tv_description)
    TextView tvDescription;

    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.iv_toolbar_weather_icon)
    ImageView weatherIcon;

    @Bind(R.id.button_post_comment)
    Button mCommentButton;
    @Bind(R.id.recycler_comments)
    RecyclerView mCommentsRecycler;

    @Bind(R.id.field_comment_text)
    EditText mCommentField;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private DatabaseReference userFishingsReference;
    private ValueEventListener mPostListener;

    private CommentAdapter mAdapter;
    private String mPostKey;
    private GoogleMap googleMap;
    private LatLng currentLocation;
    Boolean isPersonalFishing = false;


    public FishingDeatailFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        // TODO: 27.03.17 foe test. need to remove
        currentLocation = new LatLng(49.988313,36.227246);

        retrieveFishingKeyFromArgs();
        initMapFragment();
        initCommentsView();
        initDatabaseReferences();

        return view;
    }

    private void initDatabaseReferences() {
        mPostReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("fishings")
                .child(mPostKey);

        mCommentsReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("fishings-comments")
                .child(mPostKey);

        userFishingsReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("user-fishings")
                .child(getUid())
                .child(mPostKey);
    }

    private void initCommentsView() {
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentButton.setOnClickListener(this);
    }

    private void retrieveFishingKeyFromArgs() {
        Bundle bundle = getArguments();
        mPostKey = (bundle != null) ?
                bundle.getString(EXTRA_POST_KEY, "") :
                getActivity().getIntent().getStringExtra(EXTRA_POST_KEY);
        if (!mPostKey.equals(""))
            Toast.makeText(getActivity(), mPostKey, Toast.LENGTH_SHORT).show();

    }

    private void initMapFragment() {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        startRetrievingFishing();
        verifyIsPersonalFishing();
        mAdapter = new CommentAdapter(getActivity(), mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    private void verifyIsPersonalFishing() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isPersonalFishing = dataSnapshot.getValue() != null;
                // TODO: 27.03.17 show or hide DELETE item according to is it personal fishing.




            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        };
        userFishingsReference.addListenerForSingleValueEvent(eventListener);
    }

    private void startRetrievingFishing() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateUiWithData(dataSnapshot.getValue(Fishing.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load post.", Toast.LENGTH_SHORT).show();
            }
        };
        mPostReference.addValueEventListener(eventListener);
        // Keep copy of post listener so we can remove it when app stops
        mPostListener = eventListener;
    }

    private void updateUiWithData(Fishing fishing) {
        tvPlace.setText(fishing.place);
        tvDate.setText(DateUtils.getFullFriendlyDayString(getActivity(), fishing.date));
        tvWeather.setText(fishing.temperature);
        tvDescription.setText(fishing.details);
        weatherIcon.setImageResource(PrefUtils.formatWeatherSeletedToIconsCode(fishing.weatherIcon));
        Picasso
            .with(getActivity())
            .load(fishing.getPhotoUrl())
            .into(ivPhoto);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPostListener != null)
            mPostReference.removeEventListener(mPostListener);


        mAdapter.cleanupListener();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (currentLocation != null)
            showMarkerOnMap();
    }

    private void showMarkerOnMap() {
        googleMap.addMarker(new MarkerOptions()
                .title(getResources().getString(R.string.fish_place))
                .draggable(false)
                .position(currentLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

    //////////////////////////////////////// Menu //////////////////////////////////////////////////


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:

                return true;
            case R.id.delete:
                showConfirmationDialog();
                return true;
            default:
                return false;
        }
    }

    private void showConfirmationDialog() {
        DialogFragment newFragment;

        if (isPersonalFishing)
            newFragment = DeleteAlertDialog.newInstance(R.string.delete_fishing_item,
                    DeleteAlertDialog.DIALOG_DELETE_FISHING_ITEM);
         else
            newFragment = DeleteAlertDialog.newInstance(R.string.only_personal_fishing,
                    DeleteAlertDialog.DIALOG_ONLY_PERSONAL_FISHING);

        newFragment.show(getFragmentManager(), "dialog");

    }
    //////////////////////////////////////// End Menu //////////////////////////////////////////////

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_post_comment:
                postComment();
                break;
        }
    }

    private void postComment() {
        if (mCommentField.getText().toString().equals(""))
            return;

        final String uid = getUid();
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.username;

                        String commentText = mCommentField.getText().toString();
                        Comment comment = new Comment(uid, authorName, commentText);

                        mCommentsReference.push().setValue(comment);

                        mCommentField.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
////////////////////////////////////// Dialog //////////////////////////////////////////////////////
    public static class DeleteAlertDialog extends DialogFragment {

    public static final int DIALOG_DELETE_FISHING_ITEM = 500;
    public static final int DIALOG_ONLY_PERSONAL_FISHING = 600;

        public static DeleteAlertDialog newInstance(int title, int mode) {
            DeleteAlertDialog frag = new DeleteAlertDialog();
            Bundle args = new Bundle();
            args.putInt(Constants.KEY_TITLE, title);
            args.putInt(Constants.KEY_DELETE_MODE, mode);
            frag.setArguments(args);

            return frag;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            switch (getArguments().getInt(Constants.KEY_DELETE_MODE)) {
                case DIALOG_DELETE_FISHING_ITEM:
                    return new AlertDialog.Builder(getActivity())
                            .setTitle(getArguments().getInt(Constants.KEY_TITLE))
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            removeItemFromRFDB();
                                        }
                                    }
                            )
                            .setNegativeButton(android.R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    }
                            )
                            .create();

                case DIALOG_ONLY_PERSONAL_FISHING:
                    return new AlertDialog.Builder(getActivity())
                            .setTitle(getArguments().getInt(Constants.KEY_TITLE))
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            return;
                                        }
                                    }
                            ).create();
            }
            return null;
        }

        private void removeItemFromRFDB() {
            // TODO: 27.03.17 add logic for remoweing item


        }
    }
////////////////////////////////////// End Dialog //////////////////////////////////////////////////

}
