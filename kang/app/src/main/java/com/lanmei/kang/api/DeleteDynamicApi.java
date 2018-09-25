package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;
import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/24.
 * 帖子删除
 */

public class DeleteDynamicApi extends ApiV2 {

    public String id;//贴子id
    public String uid;//
    public String token;

    @Override
    protected String getPath() {
        return "posts/del";
    }

    @Override
    public AbstractApi.Method requestMethod() {
        return AbstractApi.Method.POST;
    }
}
