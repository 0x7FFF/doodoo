package com.smakhorin.doodoo;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDAO {
    private static UserDAO instance = null;
    private static String name = "";

    public static UserDAO getInstance() {
        if(instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    private static FirebaseUser user = null;

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        UserDAO.user = user;
        setUpName();
    }

    private static void setUpName() {
        DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();
        mFirebase.child("Users").child(user.getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserDAO.name = name;
    }
}
