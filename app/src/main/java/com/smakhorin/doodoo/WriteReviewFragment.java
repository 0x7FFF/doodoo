package com.smakhorin.doodoo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class WriteReviewFragment extends Fragment {
    public static WriteReviewFragment newInstance() {
        WriteReviewFragment fragment = new WriteReviewFragment();
        return fragment;
    }

    public WriteReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_review, container, false);
    }

}
