package com.radektesar.weather.android.client;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.radektesar.weather.android.RadkeTesarApplication;
import com.radektesar.weather.android.client.parser.JSONParser;
import com.radektesar.weather.android.client.response.WorldWeatherOnlineResponse;
import com.radektesar.weather.android.database.DatabaseManager;
import com.radektesar.weather.android.database.model.Forecast;
import com.radektesar.weather.android.database.model.Store;
import com.radektesar.weather.android.fragment.ForecastFragment;
import com.radektesar.weather.android.fragment.TodayFragment;
import com.radektesar.weather.android.geolocation.MyLocationListener;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Radek on 13. 3. 2015.
 */
public class LoadData extends AsyncTask<String, String, String>

    {

        JSONParser mJsonParser = new JSONParser();
        public WorldWeatherOnlineResponse mJsonUsers;
        private Context mParents = RadkeTesarApplication.getAppContext();
        public Boolean mFinishThear = false;
        MyLocationListener mMyLocationListener = new MyLocationListener();
        String mParams = null;
        boolean mConnection;
        Store mStore = new Store();
        Forecast mForecast = new Forecast();
        Gson gson = new Gson();


        private String url = "https://api2.worldweatheronline.com/free/v2/weather.ashx";
        private String ApiKey = "f8d4ac2a64026269fc77d9ac39b5d";
        private String Forecast = "5";





        @Override
        protected void onPreExecute () {
        super.onPreExecute();



    }


    protected String doInBackground(String... args) {






        //set up url in write way and find my location
        mParams = "format=json&num_of_days=" + Forecast + "&key=" + ApiKey + "&q=" + mMyLocationListener.getCityName(mParents);

        // send the request with type get with the base URL , type , information which gonna be include in URL
        JSONObject json = mJsonParser.makeHttpRequest(url, "GET", mParams);



        // check if server responed
        if(json != null){
            Log.d("Server Response: ", json.toString());
        // fix the case when server dont response or user is over access (free Api licence)
            if (json.toString().length() >= 500) {

             // mapping json into models
            mJsonUsers = gson.fromJson(json.toString(), WorldWeatherOnlineResponse.class);
            // set up controler for knowing if json was accepted
            mConnection = true;
             }

        }
        return null;
    }


    protected void onPostExecute(String file_url) {
        DatabaseManager mDatabaseManager = new DatabaseManager(mParents);

        if (mConnection) {


            int foracastDatabaseSize = 0;

            try {
                foracastDatabaseSize = mDatabaseManager.getForecasts().size();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // saving data for today into the database
            mStore.setCityCountry(mJsonUsers.getData().getRequest().get(0).getQuery());
            mStore.setTempriture(mJsonUsers.getData().getCurrentCondition().get(0).getTempC() + "°C | " + mJsonUsers.getData().getCurrentCondition().get(0).getWeatherDesc().get(0).getValue());
            mStore.setTempritureF(mJsonUsers.getData().getCurrentCondition().get(0).getTempF() + "°F | " + mJsonUsers.getData().getCurrentCondition().get(0).getWeatherDesc().get(0).getValue());
            mStore.setHumidity(mJsonUsers.getData().getCurrentCondition().get(0).getHumidity() + "%");
            mStore.setPrecipitation(mJsonUsers.getData().getCurrentCondition().get(0).getPrecipMM() + " mm");
            mStore.setPresure(mJsonUsers.getData().getCurrentCondition().get(0).getPressure() + " hPa");
            mStore.setWindSpeed(mJsonUsers.getData().getCurrentCondition().get(0).getWindspeedKmph() + " Km/h");
            mStore.setWindSpeedM(mJsonUsers.getData().getCurrentCondition().get(0).getWindspeedKmph() + " m/h");
            mStore.setWinddirection(mJsonUsers.getData().getCurrentCondition().get(0).getWinddir16Point());
            mStore.setUrlToday(mJsonUsers.getData().getCurrentCondition().get(0).getWeatherIconUrl().get(0).getValue());

            //checking if is something in database model Forecast
            // if yes it gonna save data
            //if not it gonna refresh them
            if (foracastDatabaseSize <= 2) {


                for (int i = 0; i <= 4; i++) {
                    mForecast.setForecastDayOfWeek(checkDay(i, mParents));
                    mForecast.setForecastTempritureC(mJsonUsers.getData().getWeather().get(i).getMaxtempC());
                    mForecast.setForecastTempritureF(mJsonUsers.getData().getWeather().get(i).getMaxtempF());
                    mForecast.setForecastDescription(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherDesc().get(0).getValue());
                    mForecast.setForecastUrl(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherIconUrl().get(0).getValue());

                    try {
                        mDatabaseManager.addForecast(mForecast);
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                for (int i = 0; i <= 4; i++) {
                    mForecast.setForecastDayOfWeek(checkDay(i, mParents));
                    mForecast.setForecastTempritureC(mJsonUsers.getData().getWeather().get(i).getMaxtempC());
                    mForecast.setForecastDescription(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherDesc().get(0).getValue());
                    mForecast.setForecastTempritureF(mJsonUsers.getData().getWeather().get(i).getMaxtempF());
                    mForecast.setForecastUrl(mJsonUsers.getData().getWeather().get(i).getHourly().get(2).getWeatherIconUrl().get(0).getValue());

                    try {
                        mDatabaseManager.updateForecast(mForecast);
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }

                }


            }


            try {
                mDatabaseManager.addStore(mStore);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }



            new TodayFragment();




        }else{
            //todo move it to xml
            Toast.makeText(mParents, "Server doesnt work", Toast.LENGTH_LONG);

        }
        ForecastFragment mForecastFragment = new ForecastFragment();
        mForecastFragment.onResume();
        mFinishThear = true;

    }


    public String checkDay(int i, Context context) {
        String mSomething = "";
        Calendar cal = Calendar.getInstance();

        int today = cal.get(Calendar.DAY_OF_WEEK);


        int day = today + i;

        if (day >= 8) {
            day = day - 7;
        }

        switch (day) {
            case 2: {
                mSomething = "Monday";
                return mSomething;
            }
            case 3: {
                mSomething = "Tuesday";
                return mSomething;
            }
            case 4: {
                mSomething = "Wednesday";
                return mSomething;
            }
            case 5: {
                mSomething = "Thursday";
                return mSomething;
            }
            case 6: {
                mSomething = "Friday";
                return mSomething;
            }
            case 7: {
                mSomething = "Suturday";
                return mSomething;
            }
            case 1: {
                mSomething = "Sunday";
                return mSomething;
            }
            default: {

                return mSomething;
            }
        }





    }




}
