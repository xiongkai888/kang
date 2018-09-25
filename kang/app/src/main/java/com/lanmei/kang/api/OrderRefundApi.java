package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/25.
 * 申请退款
 */

public class OrderRefundApi extends KangApi {

    public String id;//订单id
    public String uid;//用户id
    public String token;//

    @Override
    protected String getPath() {
        return "reservation/refund";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
