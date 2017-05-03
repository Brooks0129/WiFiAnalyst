package com.infor.wifi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.infor.wifi.R;
import com.infor.wifi.bean.WifiBean;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity {


    private TableLayout table;
    private TextView ssid;
    private TextView level;
    private TextView frequency;
    private TextView loss;
    private TextView delay;
    private TextView jitter;
    private RatingBar webGrade;
    private RatingBar videoGrade;
    private RatingBar chatGrade;
    private TextView latitude;
    private TextView longitude;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        WifiBean wifiBean = (WifiBean) bundle.getSerializable("wifiBean");
        setContentView(R.layout.activity_detail);
        table = (TableLayout) findViewById(R.id.table);
        ssid = (TextView) findViewById(R.id.ssid);
        level = (TextView) findViewById(R.id.level);
        frequency = (TextView) findViewById(R.id.frequency);
        loss = (TextView) findViewById(R.id.loss);
        delay = (TextView) findViewById(R.id.delay);
        jitter = (TextView) findViewById(R.id.jitter);
        webGrade = (RatingBar) findViewById(R.id.web_grade);
        videoGrade = (RatingBar) findViewById(R.id.video_grade);
        chatGrade = (RatingBar) findViewById(R.id.chat_grade);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        address = (TextView) findViewById(R.id.address);
        ssid.setText(wifiBean.WifiConnectedSSID);
        level.setText(wifiBean.WifiConnectedLevel);
        frequency.setText(wifiBean.WifiConnectedFrequency);
        loss.setText(wifiBean.packetLossRate);
        delay.setText(wifiBean.delay);
        jitter.setText(wifiBean.jitter);
        webGrade.setRating(Float.parseFloat(wifiBean.gradeWeb));
        chatGrade.setRating(Float.parseFloat(wifiBean.gradeChat));
        videoGrade.setRating(Float.parseFloat(wifiBean.gradeVideo));
        longitude.setText(wifiBean.longitude);
        latitude.setText(wifiBean.latitude);
        address.setText(wifiBean.address);
    }
}
