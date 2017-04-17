package com.infor.wifi.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.infor.wifi.R;
import com.infor.wifi.bean.User;
import com.infor.wifi.bean.WifiBean;
import com.infor.wifi.utils.MathUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LostActivity extends AppCompatActivity implements AMapLocationListener {
    private TextView tv_lost;
    private Button bt_start;
    private Button bt_upload;
    String URL = "www.baidu.com";
    private Handler handler;
    private ProgressDialog progressDialog;
    private RadioButton rb_ip;
    private EditText et_ip;
    private RadioButton rb_bd;
    private TextView tv_rec;
    private TextView tv_los;
    private String lost;
    private LinearLayout ll_yanchi;
    private TextView tv_min;
    private TextView tv_avg;
    private TextView tv_max;
    private TextView tv_mean;
    private TextView tv_doudong;
    private LinearLayout ll_doudong;
    WifiBean wifi;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    private String wifiConnectedLevel;
    private String wifiConnectedSSID;
    private String wifiConnectedFrequency;
    private ArrayList<String> listDouDong;
    private LinearLayout ll_hh;
    private AMapLocation aMapLocation;
    private Button bt_chart;
    private ArrayList<String> yanchi;
    private int pingCount = 20;
    private EditText editPingCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //初始化定位
        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        mLocationOption.setNeedAddress(true);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        setContentView(R.layout.activity_lost);
        tv_lost = (TextView) findViewById(R.id.tv_lost);
        tv_los = (TextView) findViewById(R.id.tv_los);
        bt_start = (Button) findViewById(R.id.bt_start);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        rb_ip = (RadioButton) findViewById(R.id.radioButton2);
        rb_bd = (RadioButton) findViewById(R.id.radioButton1);
        tv_min = (TextView) findViewById(R.id.tv_min);
        tv_avg = (TextView) findViewById(R.id.tv_avg);
        tv_max = (TextView) findViewById(R.id.tv_max);
        tv_mean = (TextView) findViewById(R.id.tv_mean);
        tv_doudong = (TextView) findViewById(R.id.tv_doudong);
        et_ip = (EditText) findViewById(R.id.et_ip);
        tv_rec = (TextView) findViewById(R.id.tv_rec);
        ll_yanchi = (LinearLayout) findViewById(R.id.ll_yanchi);
        ll_doudong = (LinearLayout) findViewById(R.id.ll_doudong);
        ll_hh = (LinearLayout) findViewById(R.id.ll_hh);
        bt_chart = (Button) findViewById(R.id.bt_chart);
        editPingCount = (EditText) findViewById(R.id.edit_ping_count);


        rb_ip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                et_ip.setVisibility(isChecked
                        ? View.VISIBLE
                        : View.GONE);
            }
        });
        bt_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (String s : listDouDong) {
                    double double1 = Double.parseDouble(s);
                    list.add((int) double1);
                }
                bundle.putIntegerArrayList("data", list);
                bundle.putStringArrayList("yanchi", yanchi);
                Intent intent = new Intent(LostActivity.this, ChartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rb_bd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                URL = isChecked ? "www.baidu.com" : URL;
            }
        });
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInputMethod(editPingCount);
                try {
                    String s = editPingCount.getText().toString();
                    if (s.trim().equals("")) {
                        pingCount = 20;
                    } else {
                        pingCount = Integer.parseInt(s);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(LostActivity.this, "参数不合法", Toast.LENGTH_SHORT).show();
                    return;
                }
                setEmpty();
                // Toast.makeText(LostActivity.this,et_ip.getText().toString(),Toast.LENGTH_SHORT).show();
                if (!rb_bd.isChecked()) {
                    URL = et_ip.getText().toString().equals("") ? URL : et_ip
                            .getText().toString();
                }
                progressDialog = new ProgressDialog(LostActivity.this);
                progressDialog.setMessage("正在执行ping " + URL + "命令(测试包数为" + pingCount + ")...");
                progressDialog.setCancelable(false);
                System.out.println("URL---->" + URL);
                progressDialog.show();
                new Thread() {
                    @Override
                    public void run() {
                        testLost();
                    }
                }.start();
            }

            /**
             *
             */
            private void setEmpty() {
                tv_lost.setText("");
                tv_avg.setText("");
                tv_max.setText("");
                tv_min.setText("");
                tv_mean.setText("");
                tv_doudong.setText("");
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(LostActivity.this);
                final View view = inflater.inflate(R.layout.alertdialog_dafen, null);
                final NumberPicker numberPicker1 = (NumberPicker) view.findViewById(R.id.numberPicker1);
                numberPicker1.setMinValue(0);
                numberPicker1.setMaxValue(5);
                numberPicker1.setValue(0);
                final NumberPicker numberPicker2 = (NumberPicker) view.findViewById(R.id.numberPicker2);
                numberPicker2.setMinValue(0);
                numberPicker2.setMaxValue(5);
                numberPicker2.setValue(0);
                final NumberPicker numberPicker3 = (NumberPicker) view.findViewById(R.id.numberPicker3);
                numberPicker3.setMinValue(0);
                numberPicker3.setMaxValue(5);
                numberPicker3.setValue(0);
                new AlertDialog.Builder(LostActivity.this)
                        .setTitle("请对您的网络状况进行打分")
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LayoutInflater inflater1 = LayoutInflater.from(LostActivity.this);
                                View view1 = inflater1.inflate(R.layout.alertdialog_address, null);
                                final TextView text_address_old = (TextView) view1.findViewById(R.id.text_address_old);
                                final EditText edit_address_new = (EditText) view1.findViewById(R.id.edit_address_new);
                                if (aMapLocation == null) {
                                    text_address_old.setText("系统获取定位失败，请手动输入地址^_^");
                                } else {
                                    text_address_old.setText(aMapLocation.getAddress() + ";\n您也可以在下面手动输入地址^_^");
                                }
                                new AlertDialog.Builder(LostActivity.this).setTitle("选择地址")
                                        .setView(view1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        String address;
                                        if (edit_address_new.getText() == null || edit_address_new.getText().toString().equals(""))
                                            address = aMapLocation != null ? aMapLocation.getAddress() : "";
                                        else {
                                            address = edit_address_new.getText().toString();
                                        }
                                        wifi.setAddress(address);
                                        wifi.setGradeWeb(String.valueOf(numberPicker1.getValue()));
                                        wifi.setGradeVideo(String.valueOf(numberPicker2.getValue()));
                                        wifi.setGradeChat(String.valueOf(numberPicker3.getValue()));
                                        wifi.setLongitude(String.valueOf(aMapLocation.getLongitude()));
                                        wifi.setLatitude(String.valueOf(aMapLocation.getLatitude()));
                                        String date = "";
                                        if (aMapLocation != null) {
                                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            date = df.format(new Date(aMapLocation.getTime()));//定位时间
                                        }
                                        wifi.setDate(date);
                                        if (wifiConnectedFrequency != null)
                                            wifi.setWifiConnectedFrequency(wifiConnectedFrequency);
                                        if (wifiConnectedLevel != null)
                                            wifi.setWifiConnectedLevel(wifiConnectedLevel);
                                        if (wifiConnectedSSID != null)
                                            wifi.setWifiConnectedSSID(wifiConnectedSSID);
                                        final ProgressDialog progressDialog = new ProgressDialog(LostActivity.this);
                                        progressDialog.setMessage("上传中...");
                                        progressDialog.setCancelable(true);
                                        progressDialog.show();
                                        User currentUser = BmobUser.getCurrentUser(User.class);
                                        wifi.setUserName(currentUser == null ? "" : currentUser.getUsername());
                                        wifi.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                dialog.dismiss();
                                                progressDialog.dismiss();
                                                if (e == null) {
                                                    Toast.makeText(LostActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(LostActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).create().show();
                            }
                        }).create().show();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                String rec = (String) msg.obj;
                tv_rec.setVisibility(View.VISIBLE);
                tv_los.setVisibility(View.VISIBLE);
                Bundle data = msg.getData();
                String loss = (String) data.get("loss");
                yanchi = data.getStringArrayList("yanchi");
                //ArrayList<String> doudong=data.getStringArrayList("doudong");
                String jitter = data.getString("jitter");
                //float[] douDongLv=getDouDongLv(doudong);
                ll_doudong.setVisibility(View.VISIBLE);
                tv_doudong.setText(jitter);
                //  System.out.println("yanchi--->" + yanchi);
                if (yanchi != null && yanchi.size() == 4) {
                    ll_yanchi.setVisibility(View.VISIBLE);
                    tv_min.setText(yanchi.get(0) + "ms");
                    tv_avg.setText(yanchi.get(1) + "ms");
                    tv_max.setText(yanchi.get(2) + "ms");
                    tv_mean.setText(yanchi.get(3) + "ms");
                }
                System.out.println("获得的loss是" + loss);
                tv_los.setText("丢包率:  " + loss);
                tv_lost.setText(rec);
                ll_hh.setVisibility(View.VISIBLE);
            }

            private float[] getDouDongLv(ArrayList<String> doudong) {
                ArrayList<Float> doudonglv = new ArrayList<Float>();
                if (doudong == null) {
                    return null;
                }
                if (doudong.size() == 1) {
                    float first = Float.parseFloat(doudong.get(0));
                    return new float[]{first, first, first};
                }
                for (int i = 0; i < doudong.size() - 1; i++) {
                    doudonglv.add(Math.abs(Float.parseFloat(doudong.get(i + 1))
                            - Float.parseFloat(doudong.get(i))));
                }
                float min = doudonglv.get(0);
                float max = doudonglv.get(0);
                float sum = 0;
                for (Float float1 : doudonglv) {
                    System.out.println(float1);
                    min = min < float1 ? min : float1;
                    max = max > float1 ? max : float1;
                    sum += float1;
                }
                float avg = sum / doudonglv.size();
                return new float[]{round(min), round(avg), round(max)};
            }
        };
    }

    private void getIntentData() {
        Intent intent = getIntent();
        wifiConnectedLevel = intent.getStringExtra("WifiConnectedLevel");
        wifiConnectedSSID = intent.getStringExtra("WifiConnectedSSID");
        wifiConnectedFrequency = intent.getStringExtra("WifiConnectedFrequency");
        Log.d("WifiConnectedSSID", wifiConnectedSSID);
    }

    private void testLost() {


        if (pingCount <= 0) {

        }
        // 创建本地进程
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ping -c " + pingCount + " " + URL);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // 提供进程标准输入
        BufferedReader buf = null;
        try {
            buf = new BufferedReader(new InputStreamReader(p.getInputStream(),
                    "gbk"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String str = new String();
        try {
            System.out.println(buf.readLine());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        final ArrayList<String> listLate = new ArrayList<String>();
        listDouDong = new ArrayList<String>();
        try {
            while ((str = buf.readLine()) != null) {
                sb.append(str + "\n");
                //  System.out.println(str);
                testDoudong(str, listDouDong);
                testLoss(str);
                testLate(str, listLate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   System.out.println("抖动--->" + listDouDong);
        Message message = handler.obtainMessage();
        message.obj = sb.toString();
        Bundle bundle = new Bundle();
        bundle.putString("loss", lost);
        bundle.putStringArrayList("yanchi", listLate);
        //     bundle.putStringArrayList("doudong",listDouDong);
        try {
            wifi = new WifiBean();
            wifi.setPacketLossRate(lost);
            double[] delay = new double[listDouDong.size()];
            for (int i = 0; i < delay.length; i++) {
                delay[i] = Double.parseDouble(listDouDong.get(i));
            }
            double standardDiviation = MathUtils.getStandardDiviation(delay);
            wifi.setDelay(String.format("%.3f", standardDiviation));
            double jitter = standardDiviation / MathUtils.getAverage(delay);
            wifi.setJitter(String.format("%.3f", jitter));
            bundle.putString("jitter", String.format("%.3f", jitter));
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * @param str
     */
    private void testDoudong(String str, ArrayList<String> listDouDong) {
        if (str.contains("time=")) {
            String sub = str.substring(str.indexOf("time="));
            Pattern pattern = Pattern.compile("[0-9]+(\\.?)[0-9]*");
            Matcher matcher = pattern.matcher(sub);
            while (matcher.find()) {
                listDouDong.add(matcher.group());
            }
        }
    }

    /**
     * @param str
     */
    private void testLate(String str, ArrayList<String> listLate) {
        if (str.contains("rtt")) {
            // 匹配延迟的正则表达式
            Pattern pattern = Pattern.compile("[0-9]+(\\.?)[0-9]*");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find())
                listLate.add(matcher.group());
        }
    }

    /**
     * @param str
     */
    private void testLoss(String str) {
        if (str.contains("packet loss")) {
            // 匹配丢包率的正则表达式
            Pattern pattern = Pattern.compile("[0-9]{1,3}%");
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                lost = matcher.group();
            }
            // 获取丢包率
            System.out.println("丢包率:" + lost);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                startActivity(new Intent(LostActivity.this, AboutActivity.class));
                break;
            case R.id.action_speed:
                startActivity(new Intent(LostActivity.this, SpeedActivity.class));
                break;
            case R.id.action_doudong:
                startActivity(new Intent(LostActivity.this, LostActivity.class));
                break;
            case R.id.action_yanchi:
                startActivity(new Intent(LostActivity.this, LostActivity.class));
                break;
            case R.id.action_lost:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 四舍五入保留三位小数
     *
     * @param a
     * @return
     */
    private float round(float a) {
        return (float) (Math.round(a * 1000)) / 1000;
    }

    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {

        if (aMapLocation != null) if (aMapLocation.getErrorCode() == 0) {
            this.aMapLocation = aMapLocation;
        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Toast.makeText(this, "定位错误", Toast.LENGTH_SHORT).show();
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    private void closeInputMethod(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
