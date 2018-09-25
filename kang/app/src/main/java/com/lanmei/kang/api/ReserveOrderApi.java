package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/23.
 * 预订下单
 */

public class ReserveOrderApi extends KangApi {

    public String uid;//用户id
    public String mid;//商家的uid
    public String id;//订单id
    public String items_id;//项目id
    public String guest;//人数
    public String stime;//开始时间戳
    public String etime;//结束时间戳
    public String status;//1下单(待付款)2已付款3未消费4已完成5取消订单6申请退款7退款完成
    public String pay_type;//1支付宝6余额支付7微信支付
    public String token;//
    public String pay_status;//-1有退款0未支付1支付

    @Override
    protected String getPath() {
        return "reservation/save";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
