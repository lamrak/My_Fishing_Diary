package net.validcat.fishing.net;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.validcat.fishing.models.Fishing;

public class FirebaseRepository implements Repository {
    // TODO: 10.04.17 this class should be main and common point to interact with firebase in future need to gradually move all logic.
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public FirebaseRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }


    @Override
    public void submitFishing(Fishing fishing) {

    // here I've got a fishing from cursor, ready to be flashed to firebase.!!

    }
}
