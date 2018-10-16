package com.lanmei.kang.event;

/**
 * Created by xkai on 2018/10/16.
 * 添加出库入库获取商品信息事件
 */

public class ChuKuGoodsInfoEvent {

    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public ChuKuGoodsInfoEvent(String barcode){
        this.barcode = barcode;
    }
}
