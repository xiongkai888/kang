package com.lanmei.kang.api;

/**
 * Created by xkai on 2018/2/26.
 * 商家的订单详情
 */

public class MerchantOrderDetailsApi extends KangApi {

    public String id;//订单id

    @Override
    protected String getPath() {
        return "Reservation/sellerDetail";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
