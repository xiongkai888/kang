package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;
import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/7/4.
 * 好友排名
 */

public class FriendRankApi extends ApiV2 {


    public String uid;//用户id,
    public String token;

    @Override
    protected String getPath() {
        return "friend/rank";
    }

    @Override
    public AbstractApi.Method requestMethod() {
        return AbstractApi.Method.GET;
    }
}
