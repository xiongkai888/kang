package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/20.
 * 资讯收藏
 */

public class CollectNewsApi extends KangApi {

    public String id;//资讯id
    public String del;//1 为取消收藏  不传默认收藏
    public String uid;//
    public String token;//

    @Override
    protected String getPath() {
        return "post/do_favour";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
