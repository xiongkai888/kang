package com.xson.common.bean;

public class BaseBean {

    /**
     * status : 0
     * info : 用户已禁用
     */

    private int status;
    private String info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
