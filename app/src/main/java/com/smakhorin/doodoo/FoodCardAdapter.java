package com.smakhorin.doodoo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FoodCardAdapter extends ArrayAdapter<FoodCard> {

    Context context;

    public FoodCardAdapter(Context context, int resourseId, List<FoodCard> items)
    {
        super(context,resourseId,items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        FoodCard card_item = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_win, parent, false);
        }

        TextView NamePoint = convertView.findViewById(R.id.tv_name);
        ImageView image = convertView.findViewById(R.id.iv_food);
        TextView Price = convertView.findViewById(R.id.tv_price);
        TextView HowPoint = convertView.findViewById(R.id.tv_near_count);

        HowPoint.setText(card_item.getLocationCount());
        NamePoint.setText(card_item.getName());
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
