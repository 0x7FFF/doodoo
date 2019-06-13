package com.smakhorin.doodoo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.smakhorin.doodoo.R;
import com.smakhorin.doodoo.activity.PlaceActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class WriteReviewFragment extends Fragment {

    private static WriteReviewFragment instance = null;

    private int rating = -1;
    private String text = "";

//    public static WriteReviewFragment newInstance() {
//        WriteReviewFragment fragment = new WriteReviewFragment();
//        return fragment;
//    }


    public static WriteReviewFragment getInstance() {
        if(instance == null) {
            instance = new WriteReviewFragment();
        }
        return instance;
    }

    public WriteReviewFragment() {
        // Required empty public constructor
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write_review,container,false);
        // Inflate the layout for this fragment
        final Button mSubmit = v.findViewById(R.id.btnSubmitReview);
        final EditText mText = v.findViewById(R.id.et_review);
        final RadioGroup mRating = v.findViewById(R.id.rg_rating);
        mRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    rating = Integer.parseInt(checkedRadioButton.getText().toString());
                }
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rating == -1) {
                    Toast.makeText(getActivity(),"Pick a rating",Toast.LENGTH_SHORT).show();
                    return;
                }
                text = mText.getText().toString();
                if(text.equals("")) {
                    Toast.makeText(getActivity(),"Review text is empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                ((PlaceActivity)getActivity()).submitReview();
                Toast.makeText(getActivity(),"Review Complete! ;)",Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }


}
