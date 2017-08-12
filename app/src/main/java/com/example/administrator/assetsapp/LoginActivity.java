package com.example.administrator.assetsapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.assetsapp.Bean.LoginBean;
import com.example.administrator.assetsapp.Bean.MyTestApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 登陆界面
 */


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.btnLogin)
    Button btn;
    @BindView(R.id.username)
    EditText usernameWrapper;
    @BindView(R.id.password)
    EditText passwordWrapper;
    //将数值存放在list
    List<HashMap<String, Object>> list = new ArrayList<>();

    public static final String TAG = "Main";
    public static final String API_BASE_URL = "http://112.74.212.95/php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "登录启动");
        setContentView(R.layout.login);
//        usernameWrapper.setText("admin");
//        passwordWrapper.setText("admin");
        ButterKnife.bind(this);
    }

    //登陆按钮
    @OnClick(R.id.btnLogin)
    void login() {
        String id = usernameWrapper.getText().toString();
        String pwd = passwordWrapper.getText().toString();
        if (id.equals(null) && pwd.equals(null)) {
            Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (id.equals(null) || pwd.equals(null)) {
            Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();

        } else {

//        HashMap<String, Object> map = new HashMap<>();
//        map.put("loginPwd", pwd);
//        map.put("loginName", id);
            RequestParams params = new RequestParams("http://112.74.212.95/php/loginandroid.php");
//        params.setAsJsonContent(true);
//        params.setBodyContent(map.toString());
            params.addBodyParameter("loginPwd", pwd);
            params.addBodyParameter("loginName", id);

            Callback.Cancelable cancelable
                    = x.http().get(params, new Callback.CacheCallback<String>() {
                @Override
                public boolean onCache(String result) {
                    return false;
                }

                @Override
                public void onSuccess(String result) {
                    LogUtil.d("返回：" + result);
                    if (result.contains("ok")) {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
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
            });
        }

    }
}

