package net.validcat.fishing.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentFishingListFragment extends BaseFishingListFragment{
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentPostsQuery = databaseReference.child("fishings")
                .limitToFirst(100);

        return recentPostsQuery;
    }
}
