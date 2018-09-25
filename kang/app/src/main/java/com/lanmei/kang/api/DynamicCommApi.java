package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/6/14.
 * 动态详情评论
 */

public class DynamicCommApi extends KangApi{

    public String posts_id;//帖子id
    public String content;//评论内容
    public String uid;//

    @Override
    protected String getPath() {
        return "PostsReviews/add";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
