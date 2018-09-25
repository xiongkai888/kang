package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by Administrator on 2017/5/20.
 * 首页的轮播图
 */

public class AdHomeApi extends AbstractApi {

    public int classid;//显示位置 1、首页 0、启动页

    @Override
    protected String getPath() {
        return "index/adpic";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
