package com.example.administrator.assetsapp.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/17.
 */

public class LabelBean {

    @SerializedName("CardName")
    @Expose
    private String cardName;//设备名
    @SerializedName("placeName")
    @Expose
    private String placeName;//地点
    @SerializedName("inout")
    @Expose
    private String inout;//出入
    @SerializedName("_MASK_SYNC_V2")
    @Expose
    private String mASKSYNCV2;//时间

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getInout() {
        return inout;
    }

    public void setInout(String inout) {
        this.inout = inout;
    }

    public String getMASKSYNCV2() {
        return mASKSYNCV2;
    }

    public void setMASKSYNCV2(String mASKSYNCV2) {
        this.mASKSYNCV2 = mASKSYNCV2;
    }

}
