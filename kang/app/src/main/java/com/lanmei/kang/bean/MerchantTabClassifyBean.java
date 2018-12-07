package com.lanmei.kang.bean;

import java.io.Serializable;

/**
 * Created by xkai on 2018/9/29.
 */

public class MerchantTabClassifyBean implements Serializable{


    /**
     * id : 77
     * addtime : 1531958791
     * uptime : 1538204168
     * tablename : Goods
     * classname : 	套包12800专区
     * state : 1
     * setval : 4
     * userid : 1
     * pid : 0
     * pic : null
     * img : null
     * type : 1
     */

    private String id;
    private String addtime;
    private String uptime;
    private String tablename;
    private String classname;
    private String state;
    private String setval;
    private String userid;
    private String pid;
    private String pic;
    private String img;
    private String type;

    private boolean isSelect;

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSetval() {
        return setval;
    }

    public void setSetval(String setval) {
        this.setval = setval;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
