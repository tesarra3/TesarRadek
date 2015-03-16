package com.radektesar.weather.android.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.radektesar.weather.android.R;
import com.radektesar.weather.android.RadkeTesarApplication;
import com.radektesar.weather.android.database.DatabaseManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Radek on 10. 3. 2015.
 */
public class TodayFragment extends Fragment {

    @InjectView(R.id.TodayCityState)
    TextView todayCityState;
    @InjectView(R.id.TodayTemperatureCondition)
    TextView todayTemperatureCondition;
    @InjectView(R.id.TodayHumidity)
    TextView todayHumidity;
    @InjectView(R.id.TodayPrecipitation)
    TextView todayPrecipitation;
    @InjectView(R.id.TodayPresure)
    TextView todayPresure;
    @InjectView(R.id.TodayWindSpeed)
    TextView todayWindSpeed;
    @InjectView(R.id.TodayDiction)
    TextView todayDiction;
    @InjectView(R.id.TodayIcon)
    ImageView todayIcon;


    int mStoreSize;
    int mMilemeter;
    int mDeegres;
    private DatabaseManager mDatabaseManager;
    private SharedPreferences mPrefs;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.inject(TodayFragment.this, rootView);
        mDatabaseManager = new DatabaseManager(getActivity().getBaseContext());
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());

        //Call method for set views
        setView();


        return rootView;


    }


    public void setView() {


        ButterKnife.inject(getActivity());

        mDatabaseManager = new DatabaseManager(RadkeTesarApplication.getAppContext());
        mPrefs = PreferenceManager.getDefaultSharedPreferences(RadkeTesarApplication.getAppContext());

        //Check preference
        mMilemeter = Integer.parseInt(mPrefs.getString("lenghtsUnits", "0"));
        mDeegres = Integer.parseInt(mPrefs.getString("tempritureUnits", "0"));
        try {


            //Fill view from last rec in database
            mStoreSize = mDatabaseManager.getStores().size() - 1;
            if (mDatabaseManager.getStores().size() != 0) {


                todayCityState.setText(mDatabaseManager.getStores().get(mStoreSize).getCityCountry());
                if (mDeegres != 0) {
                    todayTemperatureCondition.setText(mDatabaseManager.getStores().get(mStoreSize).getTempritureF());
                } else {
                    todayTemperatureCondition.setText(mDatabaseManager.getStores().get(mStoreSize).getTempriture());

                }
                todayHumidity.setText(mDatabaseManager.getStores().get(mStoreSize).getHumidity());
                todayPrecipitation.setText(mDatabaseManager.getStores().get(mStoreSize).getPrecipitation());
                todayPresure.setText(mDatabaseManager.getStores().get(mStoreSize).getPresure());
                if (mMilemeter != 0) {

                    todayWindSpeed.setText(mDatabaseManager.getStores().get(mStoreSize).getWindSpeedM());

                } else {
                    todayWindSpeed.setText(mDatabaseManager.getStores().get(mStoreSize).getWindSpeed());
                }

                todayDiction.setText(mDatabaseManager.getStores().get(mStoreSize).getWinddirection());

                Picasso.with(getActivity().getBaseContext()).load(mDatabaseManager.getStores().get(mStoreSize).getUrlToday()).into(todayIcon);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }


    //Set up views when fragment is open
    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    // update
    private void refreshList() {
        setView();


    }
}










