package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/7/18.
 */

public class AddFriendsApi extends ApiV2 {

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
