package com.radektesar.weather.android.client.request;

import com.google.gson.JsonParseException;
import com.radektesar.weather.android.Config;
import com.radektesar.weather.android.RadkeTesarApplication;
import com.radektesar.weather.android.client.parser.WeatherParser;
import com.radektesar.weather.android.client.response.Response;
import com.radektesar.weather.android.entity.WorldWeatherOnlineResponse;
import com.radektesar.weather.android.geolocation.MyLocationListener;


import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


/**
* Created by Radek on 16.04.2015.
*/
public class WeatherRequest extends Request
{
    private static final String REQUEST_METHOD = "GET";
    private static final String REQUEST_PATH = "free/v2/weather.ashx";
    private static final int EXPECTED_OK_RESPONSE_CODE = 200;
    MyLocationListener mMyLocationListener = new MyLocationListener();


    @Override
    public String getRequestMethod()
    {
        return REQUEST_METHOD;
    }


    @Override
    public int getExpectedOkResponseCode()
    {
        return EXPECTED_OK_RESPONSE_CODE;
    }


    @Override
    public String getAddress()
    {
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> params = new LinkedList<>();

        String paramsString = URLEncodedUtils.format(params, CHARSET);

        // url
        builder.append(Config.URL);
        builder.append(REQUEST_PATH);
        builder.append("&key=");
        builder.append(Config.API_KEY);
        builder.append("&q=");
        builder.append(mMyLocationListener.getCityName(RadkeTesarApplication.getAppContext()));
        builder.append("&tp=");
        builder.append(Config.FORECAST);

        if (paramsString != null && !paramsString.equals(""))
        {
            builder.append("?");
            builder.append(paramsString);
        }

        return builder.toString();
    }


    @Override
    public Response<WorldWeatherOnlineResponse> parseResponse(InputStream stream) throws IOException, JsonParseException
    {
        return new WeatherParser().parse(stream);
    }


    @Override
    public byte[] getContent()
    {
        return null;
    }
}
