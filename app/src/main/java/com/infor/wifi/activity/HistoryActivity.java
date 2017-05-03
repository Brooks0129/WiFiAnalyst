package com.infor.wifi.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.infor.wifi.R;
import com.infor.wifi.bean.User;
import com.infor.wifi.bean.WifiBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HistoryActivity extends AppCompatActivity {

    private List<WifiBean> list;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        initData();


    }

    private void initData() {
        list = new ArrayList<>();
        BmobQuery<WifiBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", BmobUser.getCurrentUser(User.class).getUsername());
        query.setLimit(100);
        query.findObjects(new FindListener<WifiBean>() {
            @Override
            public void done(List<WifiBean> list, BmobException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    HistoryActivity.this.list = list;
                    recyclerView.setAdapter(new HistoryAdapter(list));
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(HistoryActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class HistoryAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<WifiBean> list;

        public HistoryAdapter(List<WifiBean> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_data, parent, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.address.setText(list.get(position).address);
            holder.date.setText(list.get(position).date);
            holder.ssid.setText(list.get(position).WifiConnectedSSID);
            holder.delay.setText("延迟:" + list.get(position).delay);
            holder.loss.setText("丢包率:" + list.get(position).packetLossRate);
            holder.jitter.setText("抖动:" + list.get(position).jitter);
            holder.markChat.setText("聊天评分:" + list.get(position).gradeChat);
            holder.markVideo.setText("视频评分:" + list.get(position).gradeVideo);
            holder.markWeb.setText("网页评分:" + list.get(position).gradeWeb);
            holder.address.setText(list.get(position).address);
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wifiBean", list.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ssid;
        public TextView date;
        public TextView delay;
        public TextView loss;
        public TextView jitter;
        public TextView markChat;
        public TextView markWeb;
        public TextView markVideo;
        public TextView address;
        public View cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cardview);
            ssid = (TextView) itemView.findViewById(R.id.ssid);
            date = (TextView) itemView.findViewById(R.id.date);
            delay = (TextView) itemView.findViewById(R.id.delay);
            loss = (TextView) itemView.findViewById(R.id.loss);
            jitter = (TextView) itemView.findViewById(R.id.jitter);
            markChat = (TextView) itemView.findViewById(R.id.markChat);
            markWeb = (TextView) itemView.findViewById(R.id.markWeb);
            markVideo = (TextView) itemView.findViewById(R.id.markVideo);
            address = (TextView) itemView.findViewById(R.id.address);

        }
    }
}
