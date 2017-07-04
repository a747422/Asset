package com.example.administrator.assetsapp.Bean;


import android.accounts.Account;
import android.database.Observable;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by silver on 16-6-7.
 * blog:http://blog.csdn.net/vfush
 */
public interface MyTestApiService {

    /**
     * sayHello 动态get 参数
     *
     * @return
     */
    @GET("select.php")
    Call<String> select();
    @GET("select_asset.php")
    Call<String> label();
    @GET("select_temp.php")
    Call<String> sensor();
    @GET("select_latest_temp.php")
    Call<String> main();
    @FormUrlEncoded
    @POST("hedui.php")
    Call<String> postScan(@Field("cardId") String id);
    @FormUrlEncoded
    @POST("select.php")
    Observable<Account> postscan(@Field("tel") String tel,@Field("pwd") String id,@Field("pwd") String pwd);

}
