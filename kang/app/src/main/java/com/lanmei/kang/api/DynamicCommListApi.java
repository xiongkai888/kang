package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/6/10.
 * 动态详情评论列表（不知道是不是，猜的）
 */

public class DynamicCommListApi extends KangApi{

    public String posts_id;//查看指定贴的评论
    public String uid;//

    @Override
    protected String getPath() {
        return "PostsReviews/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
