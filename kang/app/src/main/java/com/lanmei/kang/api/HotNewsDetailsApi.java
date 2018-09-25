package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/18.
 * 热点新闻详情
 */

public class HotNewsDetailsApi extends AbstractApi {

    public String key;//关键 news 热点新闻
    public String title;//新闻标题

    @Override
    protected String getPath() {
        return "Index/news";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
