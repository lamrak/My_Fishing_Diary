package net.validcat.fishing.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.validcat.fishing.camera.CameraManager;
import net.validcat.fishing.models.FishingItem;

import java.util.HashMap;
import java.util.Map;

public class FirebaseObjectManager implements IDataObjectManager {
    private DatabaseReference mDatabase;

    @Override
    public void storeNewFishing(Context context, CameraManager cm, FishingItem item, String thumbPath, String photoPath, boolean isThingsListExists, String thingsListReference, boolean isUpdate) {
        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference("fishing");
        Map<String, Object> data= new HashMap<>();
        data.put(String.valueOf(item.getDate()), item);
        mDatabase.updateChildren(data);
    }

    @Override
    public FishingItem retrieveFishingItem(Context context, Uri uri) {
        throw new UnsupportedOperationException();
    }

    public void retrieveFishingItem(final IItemFetchedListener listener) {
        mDatabase = FirebaseDatabase.getInstance().getReference("fishing");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                FishingItem value = dataSnapshot.getValue(FishingItem.class);
                listener.onItemFetched(value);
                
                Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public interface IItemFetchedListener {
        void onItemFetched(FishingItem value);
    }
}
