package com.radektesar.weather.android.client;

import android.os.AsyncTask;


import com.radektesar.weather.android.client.request.Request;
import com.radektesar.weather.android.client.response.Response;
import com.radektesar.weather.android.utility.Logcat;

import java.lang.ref.WeakReference;


public class APICallTask extends AsyncTask<Void, Void, Response<?>>
{
    private static final int RETRY_MAX_ATTEMPTS = 1; // default value for max number of retries
    private static final long RETRY_INIT_BACKOFF = 500l; // initial sleep time before retry

    private APICall mAPICall;
    private WeakReference<APICallListener> mListener;
    private int mMaxAttempts = RETRY_MAX_ATTEMPTS;


    public APICallTask(Request request, APICallListener listener)
    {
        mAPICall = new APICall(request, this);
        setListener(listener);
    }


    public APICallTask(Request request, APICallListener listener, int maxAttempts)
    {
        this(request, listener);
        setMaxAttempts(maxAttempts);
    }


    public void setListener(APICallListener listener)
    {
        mListener = new WeakReference<>(listener);
    }


    public void setMaxAttempts(int maxAttempts)
    {
        mMaxAttempts = maxAttempts;
    }


    public Request getRequest()
    {
        return mAPICall.getRequest();
    }


    public void kill()
    {
        mAPICall.kill();
    }


    @Override
    protected Response<?> doInBackground(Void... params)
    {
        // response
        Response<?> response = null;

        // sleep time before retry
        long backoff = RETRY_INIT_BACKOFF;

        for (int i = 0; i < mMaxAttempts; i++)
        {
            APICallListener listener = mListener.get();

            Logcat.i("listener 1: ", listener==null ? "null" : "not null");

            // execute API call
            response = mAPICall.execute();

            listener = mListener.get();

            Logcat.i("listener 2: ", listener == null ? "null" : "not null");

            // success
            if (response != null)
            {
                break;
            }

            // fail
            else
            {
                if (i == mMaxAttempts) break;

                try
                {
                    Logcat.d("APICallTask.doInBackground(): sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e)
                {
                    // activity finished before we complete
                    Logcat.d("APICallTask.doInBackground(): thread interrupted so abort remaining retries");
                    Thread.currentThread().interrupt();
                    break;
                }

                // increase backoff exponentially
                backoff *= 2;
            }
        }

        return response;
    }


    @Override
    protected void onPostExecute(Response<?> response)
    {
        if (isCancelled()) return;

        APICallListener listener = mListener.get();

        Logcat.i("listener: ", listener == null ? "null" : "not null");

        if (listener != null)
        {
            if (response != null)
            {
                listener.onAPICallRespond(this, mAPICall.getResponseStatus(), response);
            }
            else
            {
                listener.onAPICallFail(this, mAPICall.getResponseStatus(), mAPICall.getException());
            }
        }
    }


    @Override
    protected void onCancelled()
    {
        Logcat.d("APICallTask.onCancelled()");
    }
}
