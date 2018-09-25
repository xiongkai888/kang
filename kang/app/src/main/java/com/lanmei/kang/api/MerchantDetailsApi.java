package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/1/29.
 * 商家详情
 */

public class MerchantDetailsApi extends ApiV2 {

    public String id;//登录用户id
    public String uid;//商家用户id
    public String lat;//纬度
    public String lon;//经度
    @Override
    protected String getPath() {
        return "place/details";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
