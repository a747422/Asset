package com.example.administrator.assetsapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.assetsapp.Bean.LabelBean;
import com.example.administrator.assetsapp.Bean.ShowListView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 出入库记录
 */

public class LabelActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, IOnSearchClickListener {

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.label_toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SearchFragment searchFragment;
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    public static final String TAG = "Main2";
    public String resp = "";
    ProgressBar bar;
    private Timer mainTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        ButterKnife.bind(this);
        toolbar.setTitle("出入库情况");//标题
        setSupportActionBar(toolbar);

        searchFragment = SearchFragment.newInstance();

        toolbar.setOnMenuItemClickListener(this);

        searchFragment.setOnSearchClickListener(this);

        getView();
        bar = (ProgressBar) findViewById(R.id.pbNormal);
        new Thread(new Runnable() {
            public void run() {
                bar.setVisibility(View.VISIBLE);
            }
        }).start();
    }

    //网络请求
    public void getView() {
        RequestParams requestParams = new RequestParams("http://112.74.212.95/php/select_asset.php");
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.length() > 5) {
                    bar.setVisibility(View.GONE);
                    list.clear();
                    list = parseJSONWithGSON(result);
                    Log.d(TAG, "list is " + list);
                    ShowListView showListView = new ShowListView(LabelActivity.this, list, R.layout.item_list_content);
                    listView.setAdapter(showListView);
                    showListView.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    // ...
                } else { // 其他错误
                    // ...
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }

    public List<HashMap<String, Object>> parseJSONWithGSON(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        Gson gson = new Gson();
        ArrayList<LabelBean> labelBeans = new ArrayList<>();
        for (JsonElement res : jsonArray) {
            LabelBean labelBean = gson.fromJson(res, new TypeToken<LabelBean>() {
            }.getType());
            labelBeans.add(labelBean);
        }

        int i = 0;
        for (LabelBean res : labelBeans) {
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
            i++;
            if (i > 3) {
                break;
            }
        }
        return list;
    }

    @OnClick(R.id.label_toolbar_back)
    void btn(View view) {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_label, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search://点击搜索
                searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
                break;
        }
        return true;
    }

    @Override
    public void OnSearchClick(String keyword) {
        Toast.makeText(this, keyword, Toast.LENGTH_SHORT).show();
        RequestParams requestParams = new RequestParams("http://112.74.212.95/php/select_asset.php");
        requestParams.addBodyParameter("搜索", keyword);
        x.http().post(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.length() > 5) {
                    bar.setVisibility(View.GONE);
                    list.clear();
                    list = parseJSONWithGSON(result);
                    Log.d(TAG, "list is " + list);
                    ShowListView showListView = new ShowListView(LabelActivity.this, list, R.layout.item_list_content);
                    listView.setAdapter(showListView);
                    showListView.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    // ...
                } else { // 其他错误
                    // ...
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    //定时器
    private void setTimerTask() {
        mainTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getView();
            }
        }, 200, 500);//表示200毫秒之后，每隔500毫秒执行一次
    }
}
