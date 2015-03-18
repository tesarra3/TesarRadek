package com.radektesar.weather.android.database.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Radek on 18. 3. 2015.
 */
public class Forecast {

    @DatabaseField(generatedId = true, columnName = "_id")
    @Expose
    protected int id;

    @Expose
    @DatabaseField
    private String forecastDayOfWeek;


    @Expose
    @DatabaseField
    private String forecastTempritureC;

    @Expose
    @DatabaseField
    private String forecastTempritureF;

    @Expose
    @DatabaseField
    private String forecastDescription;

    @Expose
    @DatabaseField
    private String forecastUrl;

    public String getForecastDayOfWeek() {
        return forecastDayOfWeek;
    }

    public void setForecastDayOfWeek(String forecastDayOfWeek) {
        this.forecastDayOfWeek = forecastDayOfWeek;
    }

    public String getForecastTempritureC() {
        return forecastTempritureC;
    }

    public void setForecastTempritureC(String forecastTempritureC) {
        this.forecastTempritureC = forecastTempritureC;
    }

    public String getForecastTempritureF() {
        return forecastTempritureF;
    }

    public void setForecastTempritureF(String forecastTempritureF) {
        this.forecastTempritureF = forecastTempritureF;
    }

    public String getForecastDescription() {
        return forecastDescription;
    }

    public void setForecastDescription(String forecastDescription) {
        this.forecastDescription = forecastDescription;
    }

    public String getForecastUrl() {
        return forecastUrl;
    }

    public void setForecastUrl(String forecastUrl) {
        this.forecastUrl = forecastUrl;
    }
}

