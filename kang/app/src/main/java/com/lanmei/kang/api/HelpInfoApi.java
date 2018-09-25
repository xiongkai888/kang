package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/18.
 * 帮助信息
 */

public class HelpInfoApi extends AbstractApi {

    public String title;

    @Override
    protected String getPath() {
        return "Index/news";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
