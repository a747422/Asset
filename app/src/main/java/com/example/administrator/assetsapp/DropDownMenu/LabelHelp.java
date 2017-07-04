package com.example.administrator.assetsapp.DropDownMenu;

import android.content.Intent;
import android.util.Log;

import com.example.administrator.assetsapp.Bean.LabelBean;
import com.example.administrator.assetsapp.Bean.LoginBean;
import com.example.administrator.assetsapp.Bean.MyTestApiService;
import com.example.administrator.assetsapp.LoginActivity;
import com.example.administrator.assetsapp.MainActivity;
import com.example.administrator.assetsapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/5/17.
 */

public class LabelHelp {
    //将数值存放在list
    List<HashMap<String, Object>> list = new ArrayList<>();

    public static final String TAG = "Main";
    public String [] CardNameData = new String[200];


//
//    public String[] CardNameData(String response) {
//        for (int i = 0; i < list.size(); i++) {
//            //再从list中获取第i个对象，存到HashMap键值队
//            HashMap<String, Object> map = list.get(i);
//            CardNameData[i] = map.get("CardName").toString();
//            Log.d(TAG, CardNameData[i]);
//        }
//        return CardNameData;
//    }
}
