package com.radektesar.weather.android.client;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.radektesar.weather.android.R;
import com.radektesar.weather.android.RadkeTesarApplication;
import com.radektesar.weather.android.client.request.Request;
import com.radektesar.weather.android.client.response.Response;
import com.radektesar.weather.android.utility.Logcat;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;


public class APICall
{

    private Request mRequest = null;
    private APICallTask mAPICallTask = null;
    private Exception mException = null;
    private ResponseStatus mResponseStatus = new ResponseStatus();

    private HttpURLConnection mConnection = null;
    //private HttpsURLConnection mConnection = null; // for SSL
    private OutputStream mRequestStream = null;
    private InputStream mResponseStream = null;


    public APICall(Request request)
    {
        mRequest = request;
    }


    public APICall(Request request, APICallTask task)
    {
        mRequest = request;
        mAPICallTask = task;
    }


    public Request getRequest()
    {
        return mRequest;
    }


    public Exception getException()
    {
        return mException;
    }


    public ResponseStatus getResponseStatus()
    {
        return mResponseStatus;
    }


    public void kill()
    {
        disconnect();
    }


    public Response execute()
    {
        try
        {
            // disables Keep-Alive for all connections
            if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
            System.setProperty("http.keepAlive", "false");

            // new connection
            byte[] requestData = mRequest.getContent();
            URL url = new URL(mRequest.getAddress());
            mConnection = (HttpURLConnection) url.openConnection();
            //mConnection = (HttpsURLConnection) url.openConnection(); // for SSL

            // ssl connection properties
            //SelfSignedSSLUtility.setupSSLConnection(mConnection, url); // for SSL using self signed certificate
            //CertificateAuthoritySSLUtility.setupSSLConnection(mConnection, url); // for SSL using certificate authority

            // connection properties
            if (mRequest.getRequestMethod() != null)
            {
                mConnection.setRequestMethod(mRequest.getRequestMethod()); // GET, POST, OPTIONS, HEAD, PUT, DELETE, TRACE
            }
//            if (mRequest.getBasicAuthToken() != null)
//            {
//                mConnection.setRequestProperty("Authorization", getBasicAuthToken(mRequest.getBasicAuthToken()));
//                mConnection.setRequestProperty("Https", "on");
//            }
//            else
//            {
                mConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            }


            //mConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Request.BOUNDARY); // for multipart
            mConnection.setRequestProperty("Accept", "application/json");
            //mConnection.setRequestProperty("Accept-Encoding", "gzip");
            mConnection.setRequestProperty("Accept-Charset", "UTF-8");
            //mConnection.setRequestProperty("Content-Length", requestData == null ? "0" : String.valueOf(requestData.length));
            //if(requestData!=null) mConnection.setChunkedStreamingMode(0);
            if (requestData != null) mConnection.setFixedLengthStreamingMode(requestData.length);
            mConnection.setConnectTimeout(30000);
            mConnection.setReadTimeout(30000);
            if (requestData != null)
            {
                // this call automatically sets request method to POST on Android 4
                // if you don't want your app to POST, you must not call setDoOutput
                // http://webdiary.com/2011/12/14/ics-get-post/
                mConnection.setDoOutput(true);
            }
            mConnection.setDoInput(true);
            mConnection.setUseCaches(false);
            mConnection.connect();

            // send request
            if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
            if (requestData != null)
            {
                mRequestStream = new BufferedOutputStream(mConnection.getOutputStream());
                mRequestStream.write(requestData);
                mRequestStream.flush();
            }

            // receive response
            if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
            String encoding = mConnection.getHeaderField("Content-Encoding");
            boolean gzipped = encoding != null && encoding.toLowerCase().contains("gzip");
            try
            {
                InputStream inputStream = mConnection.getInputStream();
                if (gzipped)
                    mResponseStream = new BufferedInputStream(new GZIPInputStream(inputStream));
                else mResponseStream = new BufferedInputStream(inputStream);
            }
            catch (FileNotFoundException e)
            {
                // error stream
                InputStream errorStream = mConnection.getErrorStream();
                if (gzipped)
                    mResponseStream = new BufferedInputStream(new GZIPInputStream(errorStream));
                else mResponseStream = new BufferedInputStream(errorStream);
            }

//            response info
            Logcat.d("APICall.connection.getURL(): " + mConnection.getURL());
            Logcat.d("APICall.connection.getContentType(): " + mConnection.getContentType());
            Logcat.d("APICall.connection.getContentEncoding(): " + mConnection.getContentEncoding());
            Logcat.d("APICall.connection.getResponseCode(): " + mConnection.getResponseCode());
            Logcat.d("APICall.connection.getResponseMessage(): " + mConnection.getResponseMessage());

            // parse response
            if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
            Response<?> response = mRequest.parseResponse(mResponseStream);
            if (response == null) throw new RuntimeException("Parser returned null response");

            if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;

            if (mConnection.getResponseCode() != getRequest().getExpectedOkResponseCode() && response.getErrorMessage() == null && response.getErrorList() == null)
            {
                response.setErrorMessage(RadkeTesarApplication.getAppContext().getString(R.string.global_general_error));
                response.setError(true);
            }

            return response;
        }
        catch (UnknownHostException e)
        {
            mException = e;
            e.printStackTrace();
            return null;
        }
        catch (FileNotFoundException e)
        {
            mException = e;
            e.printStackTrace();
            return null;
        }
        catch (SocketException e)
        {
            mException = e;
            e.printStackTrace();
            return null;
        }
        catch (SocketTimeoutException e)
        {
            mException = e;
            e.printStackTrace();
            return null;
        }
        catch (JsonParseException e)
        {
            mException = e;
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            mException = e;
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            mException = e;
            e.printStackTrace();
            return null;
        }
        finally
        {
            disconnect();
        }
    }


    private void disconnect()
    {
        try
        {
            if (mRequestStream != null) mRequestStream.close();
        }
        catch (IOException e)
        {
        }

        try
        {
            if (mResponseStream != null) mResponseStream.close();
        }
        catch (IOException e)
        {
        }

        try
        {
            // set status
            if (mConnection != null)
            {
                mResponseStatus.setStatusCode(mConnection.getResponseCode());
                mResponseStatus.setStatusMessage(mConnection.getResponseMessage());
                mConnection.disconnect();
            }
        }
        catch (Throwable e)
        {
        }

        mRequestStream = null;
        mResponseStream = null;
        mConnection = null;
    }


    private String getBasicAuthToken(String token)
    {
        return "Bearer " + token;
    }
}
