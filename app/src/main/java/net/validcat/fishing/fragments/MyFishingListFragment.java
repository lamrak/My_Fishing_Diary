package net.validcat.fishing.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.models.FishingItem;
import net.validcat.fishing.net.FirebaseRepository;
import net.validcat.fishing.tools.FishingConverter;

public class MyFishingListFragment extends BaseFishingListFragment implements GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "MyFishingListFragment";
    private static final int LOADER_ID = 3; // hz wtf?
    boolean isDbNeedSync;

    private FirebaseRepository repository;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        repository = new FirebaseRepository(getActivity());
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user-fishings")
                .child(getUid());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                FishingContract.FishingEntry.CONTENT_URI,
                FishingContract.FishingEntry.PROJECTION,
                null, null, FishingContract.FishingEntry.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            while(data.moveToNext()) {
                FishingItem item = FishingItem.createFishingItemFromCursor(data);
                repository.submitFishing(FishingConverter.convertFishingItemToFishing(item));

                Uri uri =  FishingContract.FishingEntry.buildFishingUri(item.getId());
                getActivity().getContentResolver().delete(uri, null, null);
            }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    private boolean isFishingListEmpty() {
        Cursor cursor = getActivity().getContentResolver().query(FishingContract.FishingEntry.CONTENT_URI,
                FishingContract.FishingEntry.PROJECTION, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(FishingContract.FishingEntry._ID));
                Log.d(TAG, "Database is not empty = " + id);
                cursor.close();
                return false;
            }
            cursor.close();
        }

        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


}
