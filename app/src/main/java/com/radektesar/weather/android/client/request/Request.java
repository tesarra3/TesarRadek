package com.radektesar.weather.android.client.request;

import android.os.Bundle;

import com.google.gson.JsonParseException;
import com.radektesar.weather.android.client.response.Response;


import java.io.IOException;
import java.io.InputStream;


public abstract class Request
{

    public static final String CHARSET = "UTF-8";
    public static final String BOUNDARY = "0xKhTmLbOuNdArY";

    private Bundle mMetaData = null;


    public abstract String getRequestMethod();

    public abstract int getExpectedOkResponseCode();

    public abstract String getAddress();

    public abstract Response<?> parseResponse(InputStream stream) throws IOException, JsonParseException;


    public byte[] getContent()
    {
        return null;
    }


    public String getBasicAuthUsername()
    {
        return null;
    }


    public String getBasicAuthPassword()
    {
        return null;
    }


//    //public String getBasicAuthToken()
//    {
//        return new Preferences(NotabliApplication.getContext()).getToken();
//    }


    public boolean isMultipart()
    {
        return false;
    }


    public Bundle getMetaData()
    {
        return mMetaData;
    }


    public void setMetaData(Bundle metaData)
    {
        mMetaData = metaData;
    }


}
