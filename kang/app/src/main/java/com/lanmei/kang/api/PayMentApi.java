package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/24.
 * 支付
 */

public class PayMentApi extends KangApi {

    public String uid;
    public String token;
    public String order_id;//订单id
    public int pay_type;//支付类型  1：支付宝 7：微信支付 6：余额支付

    @Override
    protected String getPath() {
        return "payment/pay";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
