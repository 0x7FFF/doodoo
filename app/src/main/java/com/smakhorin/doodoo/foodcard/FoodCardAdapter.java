package com.smakhorin.doodoo.foodcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smakhorin.doodoo.R;

import java.util.List;

public class FoodCardAdapter extends ArrayAdapter<FoodCard> {

    public FoodCardAdapter(Context context, int resourceId, List<FoodCard> items)
    {
        super(context,resourceId,items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        FoodCard card_item = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_win, parent, false);
        }

        TextView FoodName = convertView.findViewById(R.id.tv_place_name);
        ImageView image = convertView.findViewById(R.id.iv_food);
        TextView Price = convertView.findViewById(R.id.tv_price);
        TextView LocationCount = convertView.findViewById(R.id.tv_near_count);

        LocationCount.setText(card_item.getLocationCount());
        FoodName.setText(card_item.getName());
        Price.setText(card_item.getPrice());
        //image.setImageResource(R.mipmap.ic_launcher);
        switch(card_item.getImageURL()) {
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(convertView.getContext()).load(card_item.getImageURL()).into(image);
                break;
        }


        return convertView;
    }

}
