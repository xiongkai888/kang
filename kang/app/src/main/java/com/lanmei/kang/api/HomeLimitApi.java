package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by Administrator on 2017/4/24.
 */

public class HomeLimitApi extends ApiV2 {


//    public String recommend;//是否推荐 1|0
    public String lat;//纬度
    public String lon;//经度
    public String limit;//限制条数


    @Override
    protected String getPath() {
        return "index/home";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
