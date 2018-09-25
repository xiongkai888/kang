package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/8/24.
 * 订单晒单评价
 */

public class OrderEvaluationApi extends KangApi {

    public String uid;
    public String token;
    public String content;
    public String order_id;

    @Override
    protected String getPath() {
        return "PlaceReviews/add";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
