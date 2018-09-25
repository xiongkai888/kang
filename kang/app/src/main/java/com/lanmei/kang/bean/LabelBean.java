package com.lanmei.kang.bean;

/**
 * Created by Administrator on 2017/5/25.
 * 标签
 */

public class LabelBean {

    /**
     * id : 1
     * uid : 0
     * name : xxx
     */

    private String id;
    private String uid;
    private String name;
    private boolean isChoose;//是否选择标签

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {

        isChoose = choose;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
