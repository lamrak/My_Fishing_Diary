package net.validcat.fishing.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import net.validcat.fishing.R;
import net.validcat.fishing.adapters.FishingViewHolder;
import net.validcat.fishing.models.Fishing;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public abstract class BaseFishingListFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Fishing, FishingViewHolder> mAdapter;
    private LinearLayoutManager mManager;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.empty_view)
    TextView emptyView;

    public BaseFishingListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        ButterKnife.bind(this, view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        Query fishingsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Fishing, FishingViewHolder>(
                Fishing.class,
                R.layout.item_fishing_list_normal,
                FishingViewHolder.class,
                fishingsQuery) {
            @Override
            protected void populateViewHolder(final FishingViewHolder viewHolder, final Fishing model, final int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "postKey - " + postKey, Toast.LENGTH_SHORT).show();
                        // TODO: 19.03.17 change this code snippet from example to necessary logic.
                    }
                });

                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                retrieveUserAvatar(model, viewHolder);

                viewHolder.bindToPost(getActivity(), model, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference globalPostRef = mDatabase
                                .child("fishings")
                                .child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase
                                .child("user-fishings")
                                .child(model.uid)
                                .child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    private void retrieveUserAvatar(Fishing fishing, FishingViewHolder viewHolder) {
        if (fishing.getUserAvatarUrl() == null) {
            viewHolder.userAvatar.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.ic_account_circle_black_36dp));
        } else {
            Picasso.with(getActivity())
                    .load(fishing.getUserAvatarUrl())
                    .into(viewHolder.userAvatar);
        }
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Fishing f = mutableData.getValue(Fishing.class);
                if (f == null) {
                    return Transaction.success(mutableData);
                }

                if (f.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    f.starCount = f.starCount - 1;
                    f.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    f.starCount = f.starCount + 1;
                    f.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(f);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    // TODO: 19.03.17 getQuery method need for another screen that will be extends BaseFishingListFragment class for showing list of data from firebase database.
    // TODO: 19.03.17 for showing list of data from firebase database.
    //    Currently in application just one fragment that shows list data from Firebase-db.
    public abstract Query getQuery(DatabaseReference databaseReference);
}
