package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/24.
 * 帖子点赞
 */

public class LikeApi extends AbstractApi {

    public String id;//贴子id, 已点过赞的 就取消
    public String token;//

    @Override
    protected String getPath() {
        return "posts/like";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
