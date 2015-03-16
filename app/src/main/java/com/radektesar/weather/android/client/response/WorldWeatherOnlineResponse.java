
package com.radektesar.weather.android.client.response;


import com.google.gson.annotations.Expose;

public class WorldWeatherOnlineResponse {

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
