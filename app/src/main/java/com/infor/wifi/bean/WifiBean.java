package com.infor.wifi.bean;
import cn.bmob.v3.BmobObject;
/**
 * Created by Brooks on 2016/3/28.
 */
public class WifiBean extends BmobObject{
    private String packetLossRate;
    private String delay;
    private String jitter;
    private String latitude;
    private String longitude;
    private String address;
    private String WifiConnectedLevel;
    private String WifiConnectedSSID;
    private String WifiConnectedFrequency;
    private String date;
    private String gradeWeb;
    private String gradeVideo;
    private String gradeChat;
    public void setGradeChat(String gradeChat){
        this.gradeChat=gradeChat;
    }
    public void setGradeWeb(String gradeWeb){
        this.gradeWeb=gradeWeb;
    }
    public void setGradeVideo(String gradeVideo){
        this.gradeVideo=gradeVideo;
    }
    public void setDate(String date){
        this.date=date;
    }
    public void setLatitude(String latitude){
        this.latitude=latitude;
    }
    public void setLongitude(String longitude){
        this.longitude=longitude;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public void setPacketLossRate(String packetLossRate){
        this.packetLossRate=packetLossRate;
    }
    public void setDelay(String delay){
        this.delay=delay;
    }
    public void setJitter(String jitter){
        this.jitter=jitter;
    }
    public void setWifiConnectedLevel(String wifiConnectedLevel){
        WifiConnectedLevel=wifiConnectedLevel;
    }
    public void setWifiConnectedSSID(String wifiConnectedSSID){
        WifiConnectedSSID=wifiConnectedSSID;
    }
    public void setWifiConnectedFrequency(String wifiConnectedFrequency){
        WifiConnectedFrequency=wifiConnectedFrequency;
    }
}
