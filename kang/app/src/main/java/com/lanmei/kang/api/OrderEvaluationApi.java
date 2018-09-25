package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/8/24.
 * 订单晒单评价
 */

public class OrderEvaluationApi extends ApiV2 {

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
