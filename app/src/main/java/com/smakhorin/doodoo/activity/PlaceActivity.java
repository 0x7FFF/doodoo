package com.smakhorin.doodoo.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smakhorin.doodoo.fragment.GeneralFragment;
import com.smakhorin.doodoo.R;
import com.smakhorin.doodoo.UserDAO;
import com.smakhorin.doodoo.fragment.WriteReviewFragment;
import com.smakhorin.doodoo.reviews.Review;

import java.util.ArrayList;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String placeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = GeneralFragment.getInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = WriteReviewFragment.getInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        Fragment gf = GeneralFragment.getInstance();
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if(extra != null) {
            placeName = extra.getString("name");
            gf.setArguments(extra);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, gf);
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    public void submitReview() {
        UserDAO userDAO = UserDAO.getInstance();
        String userId = userDAO.getUser().getUid();
        String userName = UserDAO.getName();
        DatabaseReference currentChildDb = mDatabase.child("Reviews").child(placeName).child(userId).child("rating");
        WriteReviewFragment instance = WriteReviewFragment.getInstance();
        currentChildDb.setValue(instance.getRating());
        currentChildDb = mDatabase.child("Reviews").child(placeName).child(userId).child("text");
        currentChildDb.setValue(instance.getText());
        currentChildDb = mDatabase.child("Reviews").child(placeName).child(userId).child("name");
        currentChildDb.setValue(userName);
    }

}
