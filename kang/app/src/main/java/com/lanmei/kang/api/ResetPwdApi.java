package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/6/8.
 * 重置密码
 */

public class ResetPwdApi extends AbstractApi {

    public String phone;//手机号
    public String password;//密码

    @Override
    protected String getPath() {
        return "public/resetPwd";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
