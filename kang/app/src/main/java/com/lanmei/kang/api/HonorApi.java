package com.lanmei.kang.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2017/6/13.
 * 荣誉
 */

public class HonorApi extends AbstractApi {


    public String token;
    public String uid;
    public String status;//0 未获得  不传为全部

    @Override
    protected String getPath() {
        return "honor/index";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
