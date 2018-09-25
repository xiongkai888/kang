package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/6/8.
 * 关注（已关注就取消）
 */

public class FollowApi extends ApiV2 {

    public String mid;//关注的用户的mid或uid
    public String uid;
    public String token;


    @Override
    protected String getPath() {
        return "member_follow/follow";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
