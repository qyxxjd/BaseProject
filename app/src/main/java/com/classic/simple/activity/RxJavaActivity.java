package com.classic.simple.activity;

import android.os.Bundle;

import com.classic.simple.R;

public class RxJavaActivity extends AppBaseActivity {

    @Override public int getLayoutResId() {
        return R.layout.activity_rxjava;
    }

    @Override public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        getSupportActionBar().setTitle("RxJava示例");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
