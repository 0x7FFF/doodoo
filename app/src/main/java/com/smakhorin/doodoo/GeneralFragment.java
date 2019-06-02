package com.smakhorin.doodoo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * It's a singleton to maintain Image and Text
 */
public class GeneralFragment extends Fragment {
    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
        return fragment;
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Get view to modify it's contents
        View v = inflater.inflate(R.layout.fragment_general,container,false);
        ImageView ivPic = (ImageView) v.findViewById(R.id.iv_photo);
        Bundle extra = getArguments();
        if (extra != null) {
            photoUrl = extra.getString("photourl");
            name = extra.getString("name");
            TextView tvName = (TextView) v.findViewById(R.id.tv_name);
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
        //don't forget to return it ;)
        return v;
    }

}
