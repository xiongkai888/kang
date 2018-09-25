package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/6/16.
 * 达人关注
 */

public class ExpertAttentionApi extends AbstractApi {

    public String token;
    public String uid;

    @Override
    protected String getPath() {
        return "member_follow/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
