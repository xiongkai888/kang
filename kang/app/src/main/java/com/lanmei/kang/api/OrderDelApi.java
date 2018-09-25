package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/25.
 * 删除订单
 */

public class OrderDelApi extends KangApi {

    public String id;//订单id
    public String uid;//
    public String token;//

    @Override
    protected String getPath() {
        return "reservation/del";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
