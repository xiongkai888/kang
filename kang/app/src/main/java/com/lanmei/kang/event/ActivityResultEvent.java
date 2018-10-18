package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/10/18.
 */

public class ActivityResultEvent {

    public ActivityResultEvent(int type,String data){
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    private int type;
    private String data;
}
