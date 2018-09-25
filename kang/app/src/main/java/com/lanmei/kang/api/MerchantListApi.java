package com.lanmei.kang.api;

/**
 * Created by Administrator on 2017/5/15.
 * 商家列表(服务项目)
 */

public class MerchantListApi extends KangApi {

    public String lat;//纬度
    public String lon;//经度
    public String cid;//城市id
    public String keyword;//关键字搜索
    public String more;//1更多否则不传
    public String order;//排序 1、价格低到高 2、距离近

    @Override
    protected String getPath() {
        return "place/Placelist";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
