package com.lanmei.kang.api;


import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/5/8.
 * 登录
 */

public class LoginApi extends AbstractApi {

    public String phone;
    public String password;
    public String open_id;
    public String open_type;
    public String nickname;
    public String pic;

    @Override
    protected String getPath() {
        return "public/login";
    }

    @Override
    public AbstractApi.Method requestMethod() {
        return Method.GET;
    }
}
