package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/23.
 * 用户消费
 */

public class ConsumptionUserApi extends KangApi {

    public String uid;//
    public String id;//订单id
    public String code;//
    public long time;//
    public String token;//

    @Override
    protected String getPath() {
        return "Reservation/consume";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
