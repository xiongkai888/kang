package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/8/31.
 * 商家的 客户评价
 */

public class ClientValuateApi extends KangApi {

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
