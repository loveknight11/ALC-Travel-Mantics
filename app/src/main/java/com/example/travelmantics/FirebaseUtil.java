package com.example.travelmantics;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthStateListener;
    public static List<TravelDeal> mDeals;
    private FirebaseUtil(){}
    private static String CHILD_NAME = "traveldeals";
    private static ChildEventListener mChildEventListener;
    private static FirebaseUtil firebaseUtil;
    private static ListActivity caller;
    private static int RC_SIGN_IN = 0;
    public static Boolean isAdmin = false;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageReference;

    public static void openReference(final ListActivity callerActivity){
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            caller = callerActivity;
            mFirebaseAuth = FirebaseAuth.getInstance();
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (mFirebaseAuth.getCurrentUser() == null) {
                        signIn();
                    } else {
                        String uid = mFirebaseAuth.getUid();
                        checkAdmin(uid);
                    }
                    Toast.makeText(caller.getBaseContext(),"welcome back",Toast.LENGTH_LONG).show();
                }
            };
            connectStorage();
        }

        mDeals = new ArrayList<TravelDeal>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(CHILD_NAME);

    }

    private static void checkAdmin(String uid) {
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators").child(uid);
        ChildEventListener child = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(child);
    }

    private static void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        caller.startActivityForResult(
                 AuthUI.getInstance()
                         .createSignInIntentBuilder()
                         .setAvailableProviders(providers)
                         .build(),
                 RC_SIGN_IN);
    }

    public static void attachListener()
    {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public static void deattachListener()
    {
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    private static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference().child("deals-images");
    }
}
