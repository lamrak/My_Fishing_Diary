package net.validcat.fishing.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyFishingListFragment extends BaseFishingListFragment{

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user-fishings")
                .child(getUid());
    }
}
