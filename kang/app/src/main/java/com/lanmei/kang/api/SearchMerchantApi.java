package com.lanmei.kang.api;

/**
 * Created by xkai on 2017/7/21.
 * 搜索商家
 */

public class SearchMerchantApi extends KangApi{

    public String keyword;//关键字
    public String uid;
    public String lat;
    public String lon;

    @Override
    protected String getPath() {
        return "post/index";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
