package com.radektesar.weather.android.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radektesar.weather.android.R;
import com.radektesar.weather.android.database.model.Forecast;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Radek on 10. 3. 2015.
 */
public class ForecastAdapter extends ArrayAdapter<Forecast> {
    @InjectView(R.id.forecastDayTextview)
    TextView day;
    @InjectView(R.id.forecastTempritureTextview)
    TextView tempriture;
    @InjectView(R.id.forecastConditionTextview)
    TextView condition;
    @InjectView(R.id.forecastPictureImageView)
    ImageView picture;



    private final Activity mContext;




    public ForecastAdapter(Activity context, List<Forecast> myList) {
        super(context,R.layout.item_forecast, myList);
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(R.layout.item_forecast, null, true);
        Forecast myList = getItem(position);

        //inject of view
        ButterKnife.inject(this, convertView);

        // set up views from database
        day.setText(myList.getForecastDayOfWeek());
        tempriture.setText(myList.getForecastTempritureC());
        condition.setText(myList.getForecastDescription());

        //download picture
        Picasso.with(mContext).load(myList.getForecastUrl()).into(picture);




        return convertView;
    }
}
