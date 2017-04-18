package net.validcat.fishing.net;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.validcat.fishing.models.Fishing;
import net.validcat.fishing.models.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FirebaseRepository implements Repository {
    // TODO: 10.04.17 this class should be main and common point to interact with firebase in future need to gradually move all logic.
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseStorage storage;
    private Context context;

    public FirebaseRepository(Context context) {
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
    }


    @Override
    public void submitFishing(final Fishing fishing) {
        fishing.setUid(getUid());
        retrieveUserName(fishing);
        retrieveUserAvatarUrl(fishing);
    }


    private void retrieveUserAvatarUrl(final Fishing fishing) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null && mFirebaseUser.getPhotoUrl() == null)
            return ;

        fishing.setUserAvatarUrl(mFirebaseUser.getPhotoUrl().toString());
    }



    private void retrieveUserName(final Fishing fishing) {
        mDatabase
                .child("users")
                .child(fishing.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            Toast.makeText(context, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        fishing.setAuthor(user.username);
                        writeImageToStorageAndGetUrl(fishing);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void writeImageToStorageAndGetUrl(final Fishing fishing) {
        if (fishing.getPhotoUrl() == null)
            writeFishingWithPhotoToFRDB(fishing);
        else {
            StorageReference storageReference = storage.getReference();

            Uri file = Uri.fromFile(new File(fishing.getPhotoUrl()));

            StorageReference riversRef = storageReference
                    .child("images/" + fishing.getUid() + "/" + file.getLastPathSegment());

            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fishing.setPhotoUrl(taskSnapshot.getMetadata().getDownloadUrl().toString());
                    writeFishingWithPhotoToFRDB(fishing);
                }
            });
        }
    }

    private void writeFishingWithPhotoToFRDB(Fishing fishing) {
        String key = mDatabase.child("fishings").push().getKey();

        Map<String, Object> postValues = fishing.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/fishings/" + key, postValues);
        childUpdates.put("/user-fishings/" + fishing.getUid() + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

