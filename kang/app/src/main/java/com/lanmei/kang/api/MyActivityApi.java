package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/5/27.
 * 我的活动
 */

public class MyActivityApi extends AbstractApi {

    public String token;//
    public String status;//1、已报名 2、已参加  不传未全部

    @Override
    protected String getPath() {
        return "activity_apply/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
