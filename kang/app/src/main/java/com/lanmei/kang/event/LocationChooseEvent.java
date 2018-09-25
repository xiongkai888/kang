package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/1/31.
 * 选择地理位置
 */

public class LocationChooseEvent {

    String address;

    public String getAddress() {
        return address;
    }

    public LocationChooseEvent(String address){
        this.address = address;
    }



}
