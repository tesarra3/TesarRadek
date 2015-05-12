package com.radektesar.weather.android.client.parser;

import com.google.gson.JsonParseException;
import com.radektesar.weather.android.R;
import com.radektesar.weather.android.RadkeTesarApplication;
import com.radektesar.weather.android.client.response.Response;
import com.radektesar.weather.android.entity.BaseAPICallEntity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public abstract class Parser<T extends BaseAPICallEntity>
{

    protected abstract T getFromJson(BufferedReader reader) throws IllegalStateException;


    public Response<T> parse(InputStream stream) throws IOException, JsonParseException
    {
        Response<T> response;

        BufferedReader reader = null;
        T entity = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(stream));

            // parse JSON
            entity = getFromJson(reader);
        }
        //this can happen if the response is e.g. 404 and therefore the response is not parsable
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
                reader.close();
        }

        if (entity != null)
        {
            if(entity.getErrors() != null) {
                response = new Response<>();
                response.setError(true);
                response.setErrorList(ErrorResponseParser.parse(entity.getErrors()));
            }
            else if (entity.getMessage() != null)
            {
                response = new Response<>();
                response.setError(true);
                response.setErrorMessage(entity.getMessage());
            }
            else
            {
                response = new Response<>();
                response.setError(false);
                response.setResponseObject(entity);
            }

        }
        else
        {
            response = new Response<>();
            response.setError(true);
            response.setResponseObject(null);
            response.setErrorMessage(RadkeTesarApplication.getAppContext().getString(R.string.global_general_error));
        }

        return response;
    }

    protected String streamToString(BufferedReader reader) {
        StringBuilder total = new StringBuilder();
        String line;
        try
        {
            while ((line = reader.readLine()) != null) {
                total.append(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return total.toString();
    }


    //probably not necessary when using Gson

	/*protected static void handleUnknownParameter(JsonParser parser) throws IOException, JsonParseException
    {
		if(parser.getCurrentToken() == JsonToken.START_OBJECT)
		while(parser.nextToken() != JsonToken.END_OBJECT)
		{
			handleUnknownParameter(parser);
		}

		if(parser.getCurrentToken() == JsonToken.START_ARRAY)
		while(parser.nextToken() != JsonToken.END_ARRAY)
		{
			handleUnknownParameter(parser);
		}
	}*/

}
