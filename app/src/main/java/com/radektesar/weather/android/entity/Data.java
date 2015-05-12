
package com.radektesar.weather.android.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("current_condition")
    @Expose
    @DatabaseField
    private List<CurrentCondition> currentCondition = new ArrayList<CurrentCondition>();
    @Expose
    @DatabaseField
    private List<Request> request = new ArrayList<Request>();
    @Expose
    @DatabaseField
    private List<Weather> weather = new ArrayList<Weather>();

    /**
     * 
     * @return
     *     The currentCondition
     */
    public List<CurrentCondition> getCurrentCondition() {
        return currentCondition;
    }

    /**
     * 
     * @param currentCondition
     *     The current_condition
     */
    public void setCurrentCondition(List<CurrentCondition> currentCondition) {
        this.currentCondition = currentCondition;
    }

    /**
     * 
     * @return
     *     The request
     */
    public List<Request> getRequest() {
        return request;
    }

    /**
     * 
     * @param request
     *     The request
     */
    public void setRequest(List<Request> request) {
        this.request = request;
    }

    /**
     * 
     * @return
     *     The weather
     */
    public List<Weather> getWeather() {
        return weather;
    }

    /**
     * 
     * @param weather
     *     The weather
     */
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

}
