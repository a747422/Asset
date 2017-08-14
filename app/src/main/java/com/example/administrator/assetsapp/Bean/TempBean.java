package com.example.administrator.assetsapp.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/23.
 */

public class TempBean {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("happen_time")
    @Expose
    private String happenTime;
    @SerializedName("placeName")
    @Expose
    private String placeName;
    @SerializedName("temp")
    @Expose
    private String temp;
    @SerializedName("humidity")
    @Expose
    private String humidity;
    @SerializedName("huo")
    @Expose
    private String huo;
    @SerializedName("qi")
    @Expose
    private String qi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
    }

    public String getplaceName() {
        return placeName;
    }

    public void setplaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHuo() {
        return huo;
    }

    public void setHuo(String huo) {
        this.huo = huo;
    }

    public String getQi() {
        return qi;
    }

    public void setQi(String qi) {
        this.qi = qi;
    }

}
