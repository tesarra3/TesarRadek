package com.radektesar.weather.android.database.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Radek on 13. 3. 2015.
 */
public class Store {

    @Expose
    @DatabaseField
    private String cityCountry;

    @Expose
    @DatabaseField
    private String tempriture;

    @Expose
    @DatabaseField
    private String tempritureF;

    @Expose
    @DatabaseField
    private String humidity;

    @Expose
    @DatabaseField
    private String precipitation;

    @Expose
    @DatabaseField
    private String presure;

    @Expose
    @DatabaseField
    private String windSpeed;

    @Expose
    @DatabaseField
    private String windSpeedM;

    @Expose
    @DatabaseField
    private String winddirection;

    @Expose
    @DatabaseField
    private String urlToday;




    public String getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }

    public String getTempriture() {
        return tempriture;
    }

    public void setTempriture(String tempriture) {
        this.tempriture = tempriture;
    }

    public String getTempritureF() {
        return tempritureF;
    }

    public void setTempritureF(String tempritureF) {
        this.tempritureF = tempritureF;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getPresure() {
        return presure;
    }

    public void setPresure(String presure) {
        this.presure = presure;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindSpeedM() {
        return windSpeedM;
    }

    public void setWindSpeedM(String windSpeedM) {
        this.windSpeedM = windSpeedM;
    }

    public String getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public String getUrlToday() {
        return urlToday;
    }

    public void setUrlToday(String urlToday) {
        this.urlToday = urlToday;
    }
}
