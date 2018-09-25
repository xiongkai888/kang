package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/5/26.
 * 用户信息请求
 */

public class UserInfoApi extends KangApi {

    public String token;
    public String uid;//用户id

    @Override
    protected String getPath() {
        return "member/member";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
