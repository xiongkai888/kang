package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/20.
 * 资讯详情中的评论列表
 */

public class NewsCommentListApi extends ApiV2 {

    public String id;//资讯id
    public String uid;//
    public String token;//

    @Override
    protected String getPath() {
        return "post/reviews";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
