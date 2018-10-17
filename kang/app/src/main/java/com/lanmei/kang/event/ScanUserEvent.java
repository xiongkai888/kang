package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/10/17.
 * 扫码会员成功事件
 */

public class ScanUserEvent {
    private String result;

    public String getResult() {
        return result;
    }

    public ScanUserEvent(String result){
        this.result = result;

    }
}
