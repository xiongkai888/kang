package com.lanmei.kang.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2017/8/31.
 * 商家的 客户评价
 */

public class ClientValuateApi extends ApiV2 {

    public String mid;

    @Override
    protected String getPath() {
        return "PlaceReviews/index";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
