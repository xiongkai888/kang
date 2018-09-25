package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/7/6.
 * 充值
 */

public class RechargeApi extends AbstractApi {

    public String token;
    public String money;//充值金额
    public int pay_type;//充值类型

    @Override
    protected String getPath() {
        return "member/recharge";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
