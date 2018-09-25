package com.lanmei.kang.api;

/**
 * Created by xkai on 2018/1/29.
 * 商家信息
 */

public class MerchantInfoApi extends KangApi {

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
