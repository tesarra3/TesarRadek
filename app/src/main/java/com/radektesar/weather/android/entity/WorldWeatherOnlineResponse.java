
package com.radektesar.weather.android.entity;


import com.google.gson.annotations.Expose;

public class WorldWeatherOnlineResponse extends BaseAPICallEntity
{

    @Expose
    private Data data;

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

}
