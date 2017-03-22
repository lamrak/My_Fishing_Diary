package net.validcat.fishing.fragments;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyTopFishingListFragment extends BaseFishingListFragment{
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myUserId = getUid();

        Query myTopPostsQuery = databaseReference
                .child("user-fishings")
                .child(myUserId)
                .orderByChild("starCount");
        return myTopPostsQuery;
    }
}
