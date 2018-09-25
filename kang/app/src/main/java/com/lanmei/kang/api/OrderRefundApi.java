package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/25.
 * 申请退款
 */

public class OrderRefundApi extends ApiV2 {

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
