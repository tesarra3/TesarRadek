package com.radektesar.weather.android.client;


import com.radektesar.weather.android.client.response.Response;


public interface APICallListener
{
    public void onAPICallRespond(APICallTask task, ResponseStatus status, Response<?> response);

    public void onAPICallFail(APICallTask task, ResponseStatus status, Exception exception);
}
