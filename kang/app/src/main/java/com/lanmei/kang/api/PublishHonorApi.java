package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/6/16.
 */

public class PublishHonorApi extends AbstractApi{

    public String token;//
    public String id;//如果有表示更新
    public String first;//第一名，值为uid
    public String second;//第二名，值为uid
    public String third;//第三名，值为uid

    @Override
    protected String getPath() {
        return "honor/add";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
