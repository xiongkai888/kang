package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/25.
 * 订单详情
 */

public class OrderDetailsApi extends KangApi {

    public String id;//订单id
    public String uid;//用户id

    @Override
    protected String getPath() {
        return "reservation/detail";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
