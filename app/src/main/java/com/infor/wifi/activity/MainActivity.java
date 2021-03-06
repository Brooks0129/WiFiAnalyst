package com.infor.wifi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infor.wifi.R;
import com.infor.wifi.adapter.WifiAdapter;
import com.infor.wifi.WifiAdmin;
import com.infor.wifi.bean.User;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Timer timer;
    private String WifiConnectedLevel;
    private String WifiConnectedSSID;
    private String WifiConnectedFrequency;

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);
        final WifiAdmin wifi = new WifiAdmin(getApplicationContext());
        if (wifi.checkState() != WifiManager.WIFI_STATE_ENABLED
                && wifi.checkState() != WifiManager.WIFI_STATE_ENABLING) {
            new AlertDialog.Builder(this).setMessage("Wifi并未开启,是否立即开启？")
                    .setPositiveButton("是", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wifi.openWifi();
                        }
                    }).setNegativeButton("否", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create().show();
        }
        final ListView listView = (ListView) findViewById(R.id.lv_wifis);
        final TextView tv_con_ssid = (TextView) findViewById(R.id.tv_con_ssid);
        final TextView tv_con_ip = (TextView) findViewById(R.id.tv_con_ip);
        final LinearLayout ll_con = (LinearLayout) findViewById(R.id.ll_con);
        final LinearLayout ll_discon = (LinearLayout) findViewById(R.id.ll_discon);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        WifiAdmin wifi = new WifiAdmin(getApplicationContext());
                        if (!WifiAdmin.isWiFiActive(getApplicationContext())) {

                            if (ll_con != null) {
                                ll_con.setVisibility(LinearLayout.GONE);
                            }
                            if (ll_discon != null) {
                                ll_discon.setVisibility(LinearLayout.VISIBLE);
                            }
                        } else {
                            if (ll_discon != null) {
                                ll_discon.setVisibility(LinearLayout.GONE);
                            }
                            if (ll_con != null) {
                                ll_con.setVisibility(LinearLayout.VISIBLE);
                            }
                        }
                        wifi.startScan();
                        if (tv_con_ip != null) {
                            tv_con_ip.setText(Formatter.formatIpAddress(wifi
                                    .getIpAddress()));
                        }
                        if (tv_con_ssid != null) {
                            tv_con_ssid.setText(wifi.getSSID());
                            WifiConnectedLevel = String.valueOf(wifi.getStrength());
                            WifiConnectedSSID = String.valueOf(wifi.getSSID());
                            WifiConnectedFrequency = String.valueOf(wifi.getFrequency());
                        }
                        List<ScanResult> wifiList = wifi.getWifiList();
                        if (listView != null) {
                            listView.setAdapter(new WifiAdapter(MainActivity.this,
                                    listView, wifiList));
                        }
                    }
                });
            }
        }, 0, 5000);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer_am, R.string.open_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        TextView userName = (TextView) findViewById(R.id.user_name);
        final User currentUser = BmobUser.getCurrentUser(User.class);
        userName.setText(currentUser == null ? "未登录" : currentUser.getUsername());
        CardView loginOut = (CardView) findViewById(R.id.login_out);
        if (currentUser==null){
            TextView textView = (TextView) findViewById(R.id.tv_log_out);
            textView.setText("登录");
        }
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                BmobUser.logOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        CardView historyData = (CardView) findViewById(R.id.history_data);
        historyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = BmobUser.getCurrentUser(User.class);
                if (user == null) {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
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
        switch (id) {
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.action_speed:
                startActivity(new Intent(MainActivity.this, SpeedActivity.class));
                break;
            case R.id.action_lost:
                Intent intent = new Intent(MainActivity.this,
                        LostActivity.class);
                intent.putExtra("WifiConnectedLevel", WifiConnectedLevel);
                intent.putExtra("WifiConnectedSSID", WifiConnectedSSID);
                intent.putExtra("WifiConnectedFrequency", WifiConnectedFrequency);
                startActivity(intent);
            case R.id.action_yanchi:
                Intent intent1 = new Intent(MainActivity.this,
                        LostActivity.class);
                intent1.putExtra("WifiConnectedLevel", WifiConnectedLevel);
                intent1.putExtra("WifiConnectedSSID", WifiConnectedSSID);
                intent1.putExtra("WifiConnectedFrequency", WifiConnectedFrequency);
                startActivity(intent1);
            case R.id.action_doudong:
                Intent intent2 = new Intent(MainActivity.this,
                        LostActivity.class);
                intent2.putExtra("WifiConnectedLevel", WifiConnectedLevel);
                intent2.putExtra("WifiConnectedSSID", WifiConnectedSSID);
                intent2.putExtra("WifiConnectedFrequency", WifiConnectedFrequency);
                startActivity(intent2);
        }
        return mDrawerToggle.onOptionsItemSelected(item)
                | super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
