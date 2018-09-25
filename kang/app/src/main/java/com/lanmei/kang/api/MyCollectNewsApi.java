package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/20.
 * 我的收藏（资讯）
 */

public class MyCollectNewsApi extends KangApi {

    public String token;//
    public String cid;//
    public String uid;

    @Override
    protected String getPath() {
        return "post/favour";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
