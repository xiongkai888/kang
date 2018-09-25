package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/20.
 * 我的收藏（服务）
 */

public class MyCollectItemsApi extends KangApi {

    public String uid;//用户id

    @Override
    protected String getPath() {
        return "member/favour";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
