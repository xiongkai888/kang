package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * 注册
 */

public class RegisterApi extends AbstractApi {

    public String phone;
    public String nickname;//姓名
    public String password;//
    public String repassword;//确认密码


    @Override
    protected String getPath() {
        return "public/regist";
    }

    @Override
    public AbstractApi.Method requestMethod() {
        return Method.POST;
    }
}
