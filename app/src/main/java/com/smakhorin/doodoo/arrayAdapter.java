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

public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter(Context context, int resourseId, List<cards> items)
    {
        super(context,resourseId,items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);

        if(convertView == null)
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_win,parent,false);

        TextView NamePoint = (TextView)convertView.findViewById(R.id.NamePoint);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        TextView MoneyPay = (TextView)convertView.findViewById(R.id.MoneyPay);
        TextView HowPoint = (TextView)convertView.findViewById(R.id.HowPoint);

        HowPoint.setText(card_item.getHowPoint());
        NamePoint.setText(card_item.getNamePoint());
        MoneyPay.setText(card_item.getMoneyPay());
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
