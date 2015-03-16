package com.radektesar.weather.android.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.radektesar.weather.android.R;
import com.radektesar.weather.android.RadkeTesarApplication;
import com.radektesar.weather.android.activity.MainActivity;
import com.radektesar.weather.android.adapter.ForecastAdapter;
import com.radektesar.weather.android.database.DatabaseManager;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Radek on 10. 3. 2015.
 */
public class ForecastFragment extends Fragment {


    @InjectView(R.id.forecastListView)
    ListView forecastListView;


    private MainActivity parent;

    public ForecastFragment(){}
    private ForecastAdapter mForecastAdapter;
    DatabaseManager mDatabaseManager = new DatabaseManager(RadkeTesarApplication.getAppContext());



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (MainActivity)activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        ButterKnife.inject(ForecastFragment.this, view);



           //Call adapter(database model Forest)
       try {
            mForecastAdapter = new ForecastAdapter(parent,mDatabaseManager.getForecasts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        forecastListView.setAdapter(mForecastAdapter);




    }




}