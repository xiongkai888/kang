package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/20.
 * 我的订单列表
 */

public class OrderListApi extends KangApi {

    public String token;//
    public String uid;//用户id
    public String status;//0全部1待付款2已付款3未消费4已完成

    @Override
    protected String getPath() {
        return "Reservation/index";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
