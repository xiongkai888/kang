package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2018/1/10.
 * 动态 点赞
 */

public class DynamicLikedApi extends KangApi {

    public String id;//动态ID
    public String uid;//用户ID

    @Override
    protected String getPath() {
        return "posts/like";
    }

    @Override
    public AbstractApi.Method requestMethod() {
        return AbstractApi.Method.POST;
    }
}
