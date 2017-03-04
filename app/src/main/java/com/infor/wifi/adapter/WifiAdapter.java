package com.infor.wifi.adapter;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.custom.numberprogressbar.NumberProgressBar;
import com.infor.wifi.R;

import java.util.List;
public class WifiAdapter extends BaseAdapter{
    private Context mContext;
    private ListView mListView;
    private List<ScanResult> mWifiList;
    public WifiAdapter(Context mContext,ListView mListView,
                       List<ScanResult> mWifiList){
        this.mContext=mContext;
        this.mListView=mListView;
        this.mWifiList=mWifiList;
    }
    @Override
    public int getCount(){
        return mWifiList.size();
    }
    @Override
    public Object getItem(int position){
        return mWifiList.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder holder;
        View view=convertView;
        if (view == null) {
            view=LayoutInflater.from(mContext).inflate(R.layout.list_item,
                    null);
            holder=new ViewHolder();
            holder.nb_qiangdu=(NumberProgressBar)view
                    .findViewById(R.id.nb_qiangdu);
            holder.tv_mac=(TextView)view.findViewById(R.id.tv_mac);
            holder.tv_qiangdu=(TextView)view.findViewById(R.id.tv_qiangdu);
            holder.tv_ssid=(TextView)view.findViewById(R.id.tv_ssid);
            holder.tv_xindao=(TextView)view.findViewById(R.id.tv_xindao);
            view.setTag(holder);
        } else {
            holder=(ViewHolder)view.getTag();
        }
        ScanResult result=mWifiList.get(position);
        if (result != null) {
            holder.nb_qiangdu.setProgress((130 - Math.abs(result.level) >= 100) ? 100 :(130 - Math.abs(result.level)));
            holder.tv_ssid.setText(result.SSID);
            holder.tv_mac.setText("(" + result.BSSID + ")");
            holder.tv_xindao.setText("中心频率：" + result.frequency);
            holder.tv_qiangdu.setText(result.level + "dBm");
        }
        return view;
    }
    static class ViewHolder{
        TextView tv_ssid;
        TextView tv_mac;
        TextView tv_xindao;
        TextView tv_qiangdu;
        com.custom.numberprogressbar.NumberProgressBar nb_qiangdu;
    }
}
