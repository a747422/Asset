package com.example.administrator.assetsapp.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/25.
 */

public class ScanBean {
    @SerializedName("id")
    @Expose
    private String id;

    public ScanBean(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
