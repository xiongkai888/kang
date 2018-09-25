package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * 充值记录
 * Created by xkai on 2017/7/5.
 */

public class TopUpApi extends ApiV2 {

    public String type;//2、只看充值记录
    public String uid;
    public String token;

    @Override
    protected String getPath() {
        return "member/money_log";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
