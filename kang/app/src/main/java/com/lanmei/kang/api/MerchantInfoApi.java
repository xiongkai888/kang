package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/1/29.
 * 商家信息
 */

public class MerchantInfoApi extends ApiV2 {

    public String uid;//商家用户id
    @Override
    protected String getPath() {
        return "place/index";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
