package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/10/17.
 * 扫码销售商品成功事件
 */

public class ScanSellGoodsEvent {
    private String result;

    public String getResult() {
        return result;
    }

    public ScanSellGoodsEvent(String result){
        this.result = result;

    }
}
