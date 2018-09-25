package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/3/5.
 * 定位事件
 */

public class LocationEvent {


    private String city;
    private String longitude;
    private String latitude;

    public String getCity() {
        return city;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public LocationEvent(String city,String longitude,String latitude){
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
