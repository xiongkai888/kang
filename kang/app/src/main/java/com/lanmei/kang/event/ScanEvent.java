package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/2/5.
 * 商家扫码事件
 */

public class ScanEvent {

    private String result;

    public String getResult() {
        return result;
    }

    public ScanEvent(String result){
        this.result = result;

    }
}
