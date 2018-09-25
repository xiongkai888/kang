package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/6/6.
 * 用户信息更新
 */

public class UserUpdateApi extends KangApi {

    public String token;
    public String phone;//电话
    public String address;//地址
    public String email;//邮箱
    public String pic;//头像
    public String nickname;//用户名
    public String qq;//
    public String uid;//
    public String signature;//个性签名
    public String talent_label;//标签
    public String avatar_bg;//用户背景

    @Override
    protected String getPath() {
        return "member/update";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
