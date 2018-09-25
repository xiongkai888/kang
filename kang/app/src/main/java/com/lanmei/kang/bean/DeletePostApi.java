package com.lanmei.kang.bean;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/24.
 * 帖子删除
 */

public class DeletePostApi extends AbstractApi {

    public String id;//贴子id
    public String token;

    @Override
    protected String getPath() {
        return "posts/del";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
