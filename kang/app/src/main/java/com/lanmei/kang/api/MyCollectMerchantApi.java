package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/20.
 * 我的收藏（商家）
 */

public class MyCollectMerchantApi extends KangApi {

    public String uid;//用户id

    public String order;//排序1价格低到高2距离近

    public String lat;//纬度

    public String lon;//经度

    @Override
    protected String getPath() {
        return "place/favour";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
