package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/20.
 * 商家的订单列表
 */

public class OrderListMerchantApi extends ApiV2 {

    public String token;//
    public String uid;//用户id
    public String status;//0全部1待付款2已付款3未消费4已完成

    @Override
    protected String getPath() {
        return "Reservation/sellerOrder";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
