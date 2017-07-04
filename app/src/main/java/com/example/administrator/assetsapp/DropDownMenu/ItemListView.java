package com.example.administrator.assetsapp.DropDownMenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.assetsapp.Bean.LabelBean;
import com.example.administrator.assetsapp.Bean.MyTestApiService;
import com.example.administrator.assetsapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idtk.smallchart.chart.LineChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.Optional;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/5/18.
 */

public class ItemListView extends AppCompatActivity {
    @BindView(R.id.list_view)
    ListView listView;

    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    public static final String TAG = "Main2";
    public static final String API_BASE_URL = "http://112.74.212.95/";
    public String resp = "";
    public String[] keyStr = {"CardName", "PlaceName", "Inout", "MASKSYNCV2"};
    public int[] keyId = {R.id.tv_card_name, R.id.tv_place_name, R.id.tv_in_out, R.id.tv_mASKSYNCV2};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_view);
        getView();
//        ShowListView showListView = new ShowListView(ItemListView.this, list, R.layout.item_list_content);
//        listView.setAdapter(showListView);

    }
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_list_view, container, false);

        return view;
    }

    public void getView() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                //在build.gradle添加
                // compile 'com.squareup.retrofit2:converter-scalars:2.1.0+'
                .addConverterFactory(ScalarsConverterFactory.create())
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(new OkHttpClient())
                .build();
        MyTestApiService myTestApiService = retrofit.create(MyTestApiService.class);
        retrofit2.Call<String> Call = myTestApiService.label();
        Call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    resp = response.body().toString();
                    list = parseJSONWithGSON(resp);
                    Log.d(TAG, "list is " + list);
                    ShowListView showListView = new ShowListView(ItemListView.this, list, R.layout.item_list_content);
                    listView.setAdapter(showListView);
                    showListView.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "网络错误");
            }
        });
    }

    public List<HashMap<String, Object>> parseJSONWithGSON(String response) {
        Gson gson = new Gson();
        List<LabelBean> ressult = gson.fromJson(response, new TypeToken<List<LabelBean>>() {
        }.getType());
        for (LabelBean res : ressult) {
            Log.d(TAG, "设备名  " + res.getCardName());
            Log.d(TAG, "地点    " + res.getPlaceName());
            Log.d(TAG, "出入库  " + res.getInout());
            Log.d(TAG, "时间    " + res.getMASKSYNCV2());

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("CardName", res.getCardName());
            map.put("PlaceName", res.getPlaceName());
            map.put("Inout", res.getInout());
            map.put("MASKSYNCV2", res.getMASKSYNCV2());
            list.add(map);
        }
        return list;
    }
}
