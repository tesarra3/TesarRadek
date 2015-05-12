package com.radektesar.weather.android.client.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.radektesar.weather.android.entity.WorldWeatherOnlineResponse;

import java.io.BufferedReader;


/**
 * Created by Radek on 16.04.2015.
 */
public class WeatherParser extends Parser<WorldWeatherOnlineResponse>
{

    @Override
    protected WorldWeatherOnlineResponse getFromJson(BufferedReader reader) throws IllegalStateException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.setDateFormat(Utility.DATE_FORMAT_API);
        Gson gson = gsonBuilder.create();


        return gson.fromJson(reader, WorldWeatherOnlineResponse.class);
    }
}
