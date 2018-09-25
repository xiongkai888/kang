package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24.
 * 首页商家
 *
 */

public class HomeBean implements Serializable{


    /**
     * fee_introduction : http://qkmimages.img-cn-shenzhen.aliyuncs.com/180103/5a4cc9fc093bd.jpg
     * name : 阿迪达斯商家
     * address : 天河城
     * money : 999.00
     * area : 广州
     * lat : 23.1638170
     * lon : 113.3614560
     * distance : 12,377.20
     */

    private String fee_introduction;
    private String name;
    private String address;
    private String money;
    private String area;
    private String lat;
    private String lon;
    private String distance;
    /**
     * id : 8
     * uid : 194
     */

    private String id;
    private String uid;

    public String getFee_introduction() {
        return fee_introduction;
    }

    public void setFee_introduction(String fee_introduction) {
        this.fee_introduction = fee_introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
