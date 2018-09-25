package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/16.
 * 场地参数
 */

public class PlaceParameterBean implements Serializable{

    private double lat;//纬度
    private double lon;//经度
    private String keyword;//关键字
    private String area;//城市id
    private String order;//关键字  排序 1、价格低到高 2、距离近
    private boolean all;//一次性全部返回

    public String getArea() {
        return area;
    }

    public void setArea(String area) {

        this.area = area;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getOrder() {
        return order;
    }

    public boolean isAll() {
        return all;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setAll(boolean all) {
        this.all = all;
    }
}
