package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/6/2.
 * 足迹列表
 */

public class FootPrintApi extends AbstractApi {

    public String uid;//指定用户
    public String token;//

    @Override
    protected String getPath() {
        return "footprint/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
