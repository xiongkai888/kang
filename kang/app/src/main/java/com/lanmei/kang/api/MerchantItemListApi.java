package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/20.
 * 商家服务项目列表
 */

public class MerchantItemListApi extends KangApi {

    public String pid;//

    @Override
    protected String getPath() {
        return "member/product";
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
