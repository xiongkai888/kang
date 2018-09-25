package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/25.
 * 取消订单
 */

public class OrderCancelApi extends KangApi {

    public String id;//订单id
    public String uid;//用户id
    public String token;//

    @Override
    protected String getPath() {
        return "reservation/cancel";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
