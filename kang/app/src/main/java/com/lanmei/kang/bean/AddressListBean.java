package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/1/22.
 * 选择收货地址
 */

public class AddressListBean implements Serializable{


    /**
     * id : 4
     * name : b_15914369000
     * phone : 15914369000
     * addtime : null
     * uptime : null
     * default : 1
     * addr : 保护好环境基金
     * userid : 206
     * province : 110000
     * city : 110100
     * area : 110101
     * uname : b_15914369000
     * uphone : 15914369000
     * address : 北京市市辖区东城区保护好环境基金
     * defaultX : 1
     */

    private String id;
    private String name;
    private String phone;
    private String addtime;
    private String uptime;
    private String addr;
    private String userid;
    private String province;
    private String city;
    private String area;
    private String uname;
    private String uphone;
    private String address;
    private String defaultX;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(String defaultX) {
        this.defaultX = defaultX;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
