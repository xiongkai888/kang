package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2016/11/15.
 * 验证码
 */

public class IdentifyCodeApi extends AbstractApi {

    public String phone;
    public String code;

    @Override
    protected String getPath() {
        return "public/send_sms";
    }

    @Override
    public AbstractApi.Method requestMethod() {
        return Method.GET;
    }
}
