package com.radektesar.weather.android.client.parser;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Radek on 18. 3. 2015.
 */
public class JSONParser {

    static InputStream sIs = null;
    static JSONObject sJObj = null;
    static String sJson = null;
    static boolean sDone = false;

    // constructor
    public JSONParser() {

    }

    // function get sJson from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      String params) {

        // Making HTTP request
        try {

            // check for request method
            if(method == "POST"){
                // request method sIs POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();


                url += "?" + params;
                HttpPost httpPost = new HttpPost(url);

                //httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                sIs = httpEntity.getContent();

            }else if(method == "GET"){
                // request method sIs GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                //String paramString = URLEncodedUtils.format(params, "utf-8");


                url += "?" + params;
                HttpGet httpGet = new HttpGet(url);

                Log.d("Server Request: ", url);


                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                sIs = httpEntity.getContent();
                sIs = httpResponse.getEntity().getContent();

                //Set as true when server are responding
                sDone = true;
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();


        }
        //Fix of httpResponse
        if (sDone) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(sIs, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                sIs.close();
                sJson = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                if (sJson == null) {

                    sJObj = new JSONObject("doesntWork");
                } else {
                    sJObj = new JSONObject(sJson);
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
        }else{
            try {
                sJObj = new JSONObject().put("JSON", "Hello, World!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // return JSON String
        return sJObj;

    }



}
