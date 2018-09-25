package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/7/4.
 * 签到
 */

public class SignInApi extends AbstractApi{


    public String token;

    @Override
    protected String getPath() {
        return "member/signIn";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
