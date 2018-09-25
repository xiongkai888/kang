package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/20.
 * 资讯详情中的评论列表
 */

public class NewsCommentListApi extends KangApi {

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
