package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/6/15.
 * 达人好友列表
 */

public class GoodFriendApi extends KangApi {


    public String uid;//用户id,
    public String mid;//id,
    public String type;//1、新朋友 2、感兴趣的  不放为好友
    public String token;

    @Override
    protected String getPath() {
        return "friend/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
