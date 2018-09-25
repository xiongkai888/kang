package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/25.
 * 标签
 */

public class LabelApi extends AbstractApi {

    public String token;
    public String uid;
    public String name;
    public String keyword;//搜索标签

    @Override
    protected String getPath() {
        return "talent/label";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
