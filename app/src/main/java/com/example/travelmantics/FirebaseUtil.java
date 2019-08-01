package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    public static List<TravelDeal> mDeals;
    private FirebaseUtil(){}
    private static String CHILD_NAME = "traveldeals";
    private static ChildEventListener mChildEventListener;
    private static FirebaseUtil firebaseUtil;

    public static void openReference(){
        if (firebaseUtil == null) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDeals = new ArrayList<TravelDeal>();
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(CHILD_NAME);
    }
}
