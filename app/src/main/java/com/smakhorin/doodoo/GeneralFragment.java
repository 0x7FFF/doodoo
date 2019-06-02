package com.smakhorin.doodoo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * It's a singleton to maintain Image and Text
 */
public class GeneralFragment extends Fragment {
    private String photoUrl,name;

    private static GeneralFragment instance = null;

    public GeneralFragment() {
        // Required empty public constructor
    }

    public static GeneralFragment getInstance() {
        if(instance == null) {
            instance = new GeneralFragment();
        }
        return instance;
    }


    private ReviewsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Get view to modify it's contents
        View v = inflater.inflate(R.layout.fragment_general,container,false);
        //Extra (pic and name)
        ImageView ivPic = (ImageView) v.findViewById(R.id.iv_place_photo);
        Bundle extra = getArguments();
        if (extra != null) {
            photoUrl = extra.getString("photourl");
            name = extra.getString("name");
            TextView tvName = (TextView) v.findViewById(R.id.tv_place_name);
            tvName.setText(name);
        }
        try {
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.img_nophoto)
                    .into(ivPic);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //RecyclerView
        RecyclerView mReviews = v.findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mReviews.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReviews.getContext(), layoutManager.getOrientation());
        mReviews.addItemDecoration(dividerItemDecoration);
        mAdapter = new ReviewsAdapter();
        mReviews.setAdapter(mAdapter);
        getAllReviews();
        //don't forget to return it ;)
        return v;
    }


    private void getAllReviews() {
        DatabaseReference currentChildDb = FirebaseDatabase.getInstance().getReference().child("Reviews").child(name);
        currentChildDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    mAdapter.clearReviews();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> val = (HashMap<String, Object>) childSnapshot.getValue();
                        String name = val.get("name").toString();
                        String rating = val.get("rating").toString();
                        String text = val.get("text").toString();
                        Review review = new Review(name, rating, text);
                        mAdapter.addReview(review);
                    }
                }
                else {
                    Review review = new Review("Could be you!","5","No reviews yet! Be the first one to write it");
                    mAdapter.addReview(review);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
