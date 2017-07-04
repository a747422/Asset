package com.example.administrator.assetsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 关于我们
 */

public class AdoutActivity extends AppCompatActivity {
    @BindView(R.id.adout_toolbar_back)
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.adout_toolbar_back)
    void back(View view){
        this.finish();
    }
}
