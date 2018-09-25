package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/7/4.
 * 达人粉丝
 */

public class ExpertFansApi extends AbstractApi {

    public String token;
    public String uid;

    @Override
    protected String getPath() {
        return "member_follow/fans";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
