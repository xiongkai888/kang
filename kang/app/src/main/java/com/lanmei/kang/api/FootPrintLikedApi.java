package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/7/25.
 *
 * 足迹详情 评论列表 点赞
 */

public class FootPrintLikedApi extends AbstractApi {

    public String id;//列表id, 已点过赞的 就取消
    public String token;

    @Override
    protected String getPath() {
        return "footprint/like";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
