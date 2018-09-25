package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/23.
 * 商家消费
 */

public class ConsumptionMerchantApi extends KangApi {

    public String uid;//
    public String id;//订单id
    public String code;//
    public String time;//
    public String token;//

    @Override
    protected String getPath() {
        return "Reservation/service";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
