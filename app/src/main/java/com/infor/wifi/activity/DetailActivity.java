package com.infor.wifi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.infor.wifi.R;

public class DetailActivity extends AppCompatActivity {

    private Bundle wifiBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiBean = getIntent().getBundleExtra("wifiBean");
        setContentView(R.layout.activity_detail);
    }
}
