package net.validcat.fishing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.models.FishingItem;
import net.validcat.fishing.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoogleSignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG = GoogleSignInFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private static final int LOADER_ID = 3; // hz wtf?

    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.sign_in_button)
    SignInButton btnSignIn;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_sync_descr)
    TextView tvSyncDescr;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabase;
    private GoogleApiClient apiClient;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_sign, container, false);
        ButterKnife.bind(this, view);
        configureGoogleSignIn();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                else Log.d(TAG, "onAuthStateChanged:signed_out");

                updateUI(user);
            }
        };

        return view;
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        apiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getActivity(), FishingListActivity.class));
            getActivity().finish();
        } else {
            tvSyncDescr.setText(R.string.keep_sync);
            tvStatus.setText(R.string.signed_out);
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.e(TAG, "Google Sign-In was successful, authenticate with Firebase.");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        onAuthSuccess(firebaseAuth.getCurrentUser());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user);
    }

    private void writeNewUser(FirebaseUser fireUser) {
        User user = new User(fireUser.getDisplayName(),
                fireUser.getEmail(),
                fireUser.getPhotoUrl().toString());


        mDatabase
                .child("users")
                .child(fireUser.getUid())
                .setValue(user);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

        if (firebaseAuth.getCurrentUser() != null) {
            onAuthSuccess(firebaseAuth.getCurrentUser());
        }

        checkIsEmptyInternalStorage();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    ///////////////////////////////// Loading fishing from DB //////////////////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                FishingContract.FishingEntry.CONTENT_URI,
                FishingContract.FishingEntry.PROJECTION,
                null, null, FishingContract.FishingEntry.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: data is null - " + (data == null) );
//        data.moveToFirst();
        while(data.moveToNext()) {
            getFishingFromCursore(data);
        }
    }

    public void getFishingFromCursore(Cursor cursor) {
        FishingItem item = FishingItem.createFishingItemFromCursor(cursor);
        Log.d(TAG, "itemId - " + item.getId());
        Log.d(TAG, "getDescription -  " + item.getDescription() + "\n\n");
        // TODO: 07.04.17 need to implement logic for sawing data from cursor to firebase.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void checkIsEmptyInternalStorage() {
        boolean isEmpty = isFishingListEmpty();
        Toast.makeText(getActivity(), "isEmpty - " + isEmpty, Toast.LENGTH_SHORT).show();
    }

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
}
