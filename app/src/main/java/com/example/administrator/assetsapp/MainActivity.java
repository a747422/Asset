package com.example.administrator.assetsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.assetsapp.Bean.MainBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.activity.PermissionUtils;

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

import static android.view.View.*;
/**
 *      主页面
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener {
    @BindView(R.id.llSensor)
    LinearLayout llSensor;
    @BindView(R.id.llScan)
    LinearLayout llScan;
    @BindView(R.id.llLabel)
    LinearLayout llLabel;
    @BindView(R.id.llAdout)
    LinearLayout llAdout;

    @BindView(R.id.tvTSite)
    TextView tvTSite;
    @BindView(R.id.tvTTime)
    TextView tvTTime;
    @BindView(R.id.tvTData)
    TextView tvTData;
    @BindView(R.id.tvHSite)
    TextView tvHSite;
    @BindView(R.id.tvHTime)
    TextView tvHTime;
    @BindView(R.id.tvHData)
    TextView tvHData;
    @BindView(R.id.tvGSite)
    TextView tvGSite;
    @BindView(R.id.tvGTime)
    TextView tvGTime;
    @BindView(R.id.tvGData)
    TextView tvGData;
    @BindView(R.id.tvFSite)
    TextView tvFSite;
    @BindView(R.id.tvFTime)
    TextView tvFTime;
    @BindView(R.id.tvFData)
    TextView tvFData;

    public static final int REQUEST_CODE = 0;
    //记录第一次点击的时间
    private long clickTime = 0;
    public static final String TAG = "Main2";
    public static final String API_BASE_URL = "http://112.74.212.95/php/select_latest_temp.php";
    public String resp = "";
    //将数值存放在list
    List<HashMap<String, Object>> list = new ArrayList<>();

    private Timer mainTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainTimer = new Timer();
       // setTimerTask();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

      // getview();
        initView();

    }

    private void initView() {
        llSensor.setOnClickListener(this);
        llScan.setOnClickListener(this);
        llLabel.setOnClickListener(this);
        llAdout.setOnClickListener(this);
    }

////网络请求，使用Xutils框架
//    public void getview() {
//        RequestParams requestParams = new RequestParams(API_BASE_URL);
//        x.http().get(requestParams, new Callback.CacheCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                if (result.length() > 5) {
//                    Log.d(TAG, result);
//                    parseJSONWithGSON(result);
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
//                if (ex instanceof HttpException) { // 网络错误
//                    HttpException httpEx = (HttpException) ex;
//                    int responseCode = httpEx.getCode();
//                    String responseMsg = httpEx.getMessage();
//                    String errorResult = httpEx.getResult();
//                    // ...
//                } else { // 其他错误
//                    // ...
//                }
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//
//            @Override
//            public boolean onCache(String result) {
//                return false;
//            }
//        });
//
//        }
////数据解析显示
//    private void parseJSONWithGSON(String response) {
//
//        int k = 0, i = 0;
//        Gson gson = new Gson();
//        List<MainBean> ressult = gson.fromJson(response, new TypeToken<List<MainBean>>() {
//        }.getType());
//        for (MainBean res : ressult) {
//            Log.d(TAG, "id is " + res.getHappenTime());
//            Log.d(TAG, "pwd is " + res.getPlaceName());
//            tvTSite.setText(res.getPlaceName());
//            tvTTime.setText(res.getHappenTime());
//            tvTData.setText(res.getTemp() + "°");
//            tvHSite.setText(res.getPlaceName());
//            tvHTime.setText(res.getHappenTime());
//            tvHData.setText(res.getHumidity() + "%");
//            tvGSite.setText(res.getPlaceName());
//            tvGTime.setText(res.getHappenTime());
//            tvGData.setText(res.getQi());
//            if(res.getQi().contains("异常")){
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
////                        dialog.setIcon(R.drawable.check_bg)
//                dialog.setTitle("提示");
//                dialog.setMessage(res.getPlaceName()+"地点，气体异常，请马上前往地点查看！");
//                dialog.setPositiveButton("确定", new
//                        DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        });
//
//                dialog.show();
//            }
//            tvFSite.setText(res.getPlaceName());
//            tvFTime.setText(res.getHappenTime());
//            tvFData.setText(res.getHuo());
//            if(res.getQi().contains("异常")){
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog.setTitle("提示");
//                dialog.setMessage(res.getPlaceName()+"地点，火焰异常，请马上前往地点查看！");
//                dialog.setPositiveButton("确定", new
//                        DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        });
//
//                dialog.show();
//            }
//        }
//
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //跳转页面
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llSensor:
                Intent inSensor = new Intent(MainActivity.this, SensorActivity.class);
                startActivity(inSensor);

                break;
            case R.id.llScan:
                showCamera(view);
                Intent inScan = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(inScan);

                break;
            case R.id.llLabel:
                Intent inLabel = new Intent(MainActivity.this, LabelActivity.class);
                startActivity(inLabel);

                break;
            case R.id.llAdout:
                Intent inAdout = new Intent(MainActivity.this, AdoutActivity.class);
                startActivity(inAdout);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Toast.makeText(MainActivity.this, scanResult, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when the 'show camera' button is clicked.
     * Callback is defined in resource layout definition.
     */
    public void showCamera(View view) {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_CAMERA:
                    Log.d(TAG, "相机打开成功");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }


    //点击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//判断是否退出
    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "提示：再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "exit application");
            this.finish();
        }
    }

//    private void setTimerTask() {
//        mainTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                getview();
//            }
//        }, 200, 500);//表示200毫秒之后，每隔500毫秒执行一次
//    }
}
