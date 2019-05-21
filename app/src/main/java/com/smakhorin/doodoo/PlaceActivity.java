package com.smakhorin.doodoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

public class PlaceActivity extends AppCompatActivity {

    HashMap<String,String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String photoUrl = extra.getString("photourl");
            String name = extra.getString("name");
            String imageUrl = "https://maps.googleapis.com/maps/api/place/photo" + "?maxwidth=1280" +
                    "&maxheight=960" +
                    "&photoreference=" + photoUrl +
                    "&key=AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk";
            ImageView ivPic = (ImageView) findViewById(R.id.iv_photo);
            try {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.img_nophoto)
                        .into(ivPic);
            }
            catch (Exception e) {
                TextView tvError = findViewById(R.id.tv_nophoto);
                tvError.setVisibility(View.VISIBLE);
            }
            TextView tvName = (TextView) findViewById(R.id.tv_name);
            tvName.setText(name);
        }
    }
}
