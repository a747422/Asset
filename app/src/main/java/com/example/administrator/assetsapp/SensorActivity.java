package com.example.administrator.assetsapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.assetsapp.Bean.TempBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/5/8.
 */

public class SensorActivity extends BaseActivity {
    @BindView(R.id.fragment_toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.chart_view_1)
    ChartView mChartView1;
    @BindView(R.id.chart_view_2)
    ChartView mChartView2;
    @BindView(R.id.Tv_huo)
    TextView tvH;
    @BindView(R.id.Tv_qi)
    TextView tvQ;

    public static final String TAG = "Main2";
    public static final String API_BASE_URL = "http://112.74.212.95/php/";
    private Timer mainTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Log.d(TAG, "启动");
        ButterKnife.bind(this);
        mainTimer = new Timer();
        setTimerTask();
        setData();
    }

    //数据采集显示
    private void setData() {
        Log.d(TAG, "启动");
        final String title = "最新温度数据表(° )";
        final String title2 = "最新湿度数据表(% )";
        final String[] xLabel1 = new String[7];
        final String[] xLabel2 = new String[7];
        final String[] data1 = new String[7];
        final String[] data2 = new String[7];
        final String[] qi = new String[7];
        final String[] huo = new String[7];
        final String[] time = new String[7];
        RequestParams params = new RequestParams("http://112.74.212.95/php/select_temp.php");

        Callback.Cancelable cancelable
                = x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String resp) {
                if (resp.length() > 10) {
                    Log.d(TAG, resp);
                    int j = 6;
                    Gson gson = new Gson();
                    List<TempBean> ressul = gson.fromJson(resp, new TypeToken<List<TempBean>>() {
                    }.getType());
                    for (TempBean res : ressul) {
                        Log.d(TAG, "Temp is " + res.getTemp());
                        Log.d(TAG, "Humidity is " + res.getHumidity());
                        String str = res.getHappenTime().toString().substring(11);
                        time[j] = res.getHappenTime().toString().substring(0, 10);
                        Log.d(TAG, "Time is " + time[j]);
                        Log.d(TAG, "str is " + str);
                        xLabel1[j] = str;
                        xLabel2[j] = str;
                        data1[j] = res.getTemp();
                        data2[j] = res.getHumidity();
                        qi[j] = res.getQi();
                        huo[j] = res.getHuo();
                        j--;
                    }
                    mChartView1.setTitle(time[6] + " " + title);
                    mChartView1.setxLabel(xLabel1);
                    mChartView1.setData(data1);
                    mChartView1.fresh();
                    mChartView2.setTitle(time[6] + " " + title2);
                    mChartView2.setxLabel(xLabel2);
                    mChartView2.setData(data2);
                    mChartView2.fresh();
                    tvQ.setText("气体传感器当前情况：" + qi[6].toString());
                    tvH.setText("火焰传感器当前情况：" + huo[6].toString());
                    if (qi[6].toString().contains("异常")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SensorActivity.this);
//                        dialog.setIcon(R.drawable.check_bg)
                        dialog.setTitle("提示");
                        dialog.setMessage(qi[6].toString() + "地点，气体异常，请马上前往地点查看！");
                        dialog.setPositiveButton("确定", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });

                        dialog.show();
                    }

                    if (huo[6].toString().contains("异常")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SensorActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage(huo[6].toString() + "地点，火焰异常，请马上前往地点查看！");
                        dialog.setPositiveButton("确定", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                        dialog.show();
                    }
                } else

                {
                    Log.d(TAG, "超时");
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
                Toast.makeText(x.app(), "取消", Toast.LENGTH_LONG).show();
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

    @OnClick(R.id.fragment_toolbar_back)
    void btn() {
        finish();
    }

    //定时请求数据
    private void setTimerTask() {
        mainTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setData();
            }
        }, 200, 500);//表示200毫秒之后，每隔500毫秒执行一次
    }
}
