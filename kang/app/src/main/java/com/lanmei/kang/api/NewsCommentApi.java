package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/5/20.
 * 资讯详情提交评论
 */

public class NewsCommentApi extends ApiV2 {

    public String id;//资讯id
    public String content;//评论内容
    public String uid;//

    @Override
    protected String getPath() {
        return "post/do_reviews";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
