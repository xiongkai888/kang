package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/7/18.
 */

public class AddFriendsApi extends KangApi {

    public String uid;//发起添加好友的用户id
    public String mid;//用户id
    public String token;

    @Override
    protected String getPath() {
        return "friend/add";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
