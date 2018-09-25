package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/6/14.
 * 资讯详情
 */

public class NewsDetailsApi extends ApiV2 {

    public String id;//资讯id
    public String uid;
    public String token;//

    @Override
    protected String getPath() {
        return "post/details";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
