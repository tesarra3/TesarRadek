package com.radektesar.weather.android;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;



/**
 * Created by Radek on 18. 3. 2015.
 */
public class RadkeTesarApplication extends Application {


    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        // before first  activity load data

        //check network and set up asyncTask

    }


    // set up context for hole applinacation
    public static Context getAppContext() {
        return RadkeTesarApplication.context;
    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static void toastTool(Context context, String texts) {
        Toast.makeText(context, texts, Toast.LENGTH_LONG).show();
    }


}