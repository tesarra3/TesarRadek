package com.radektesar.weather.android.geolocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by Radek on 18. 3. 2015.
 */
public class MyLocationListener implements LocationListener {

    public String mCityName;
    public String mNameOfCity = "";
    public LocationManager mLocationManager;

    Location mLocation;




    public String getCityName(Context context) {


        try {
            mLocationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                // First get mLocation from Network Provider

                if (isNetworkEnabled) {

                    Log.d("Network", "Network");
                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }
                }
                //get the mLocation by gps
                if (isGPSEnabled) {
                    if (mLocation == null) {

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mLocationManager != null) {
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        }
                    }
                }
            }

            // cut only first 4 digits because of geocoder

            double lat = Double.valueOf(String.valueOf(mLocation.getLatitude()).substring(0, Math.min(String.valueOf(mLocation.getLatitude()).length(), 5)));
            double lon = Double.valueOf(String.valueOf(mLocation.getLongitude()).substring(0, Math.min(String.valueOf(mLocation.getLongitude()).length(), 5)));

            // gecoder with the reduce information is faster
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(lat, lon, 1);




            //
            mNameOfCity = addresses.get(0).getLocality();



            //fix the case when geocoder doesnt work
            mCityName = mLocation.getLatitude() +","+ mLocation.getLongitude();
            if(!mNameOfCity.isEmpty()){
                mCityName = mNameOfCity;}


            //replace of white space
            mCityName = mCityName.replace(" ", "%20");





        } catch (Exception e) {
            e.printStackTrace();
        }



        return mCityName;
    }







    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
